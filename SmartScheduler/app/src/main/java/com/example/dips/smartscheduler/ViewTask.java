package com.example.dips.smartscheduler;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewTask extends Fragment {

    public static FragmentActivity TaskFragActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fetch phoneNumber through SharedPreferences
        RelativeLayout TaskTabRelLayout = (RelativeLayout) inflater.inflate(R.layout.activity_view_task, container, false);
        TaskFragActivity = super.getActivity();

        SharedPreferences prefs = TaskFragActivity.getSharedPreferences("Data", 0x0000);
        int phoneNumber = prefs.getInt("phoneNumber", 1);

        Log.d("Phone Number :", String.valueOf(phoneNumber));
        //create DatabaseHelper
        DatabaseHelper dbHelper=new DatabaseHelper(this.getContext());

        String[] eventNames=dbHelper.getTaskList(phoneNumber);

        //set NOTASKDATA text view visibilty property
        TextView txtNoGroup= (TextView) TaskTabRelLayout.findViewById(R.id.txtViewTaskData);
        if(eventNames.length==0){
            Log.d("Event Size:", String.valueOf(eventNames.length));
            txtNoGroup.setVisibility(View.VISIBLE);
        }

        //populate list
        ListView viewListTask=(ListView)TaskTabRelLayout.findViewById(R.id.viewTaskListView);
        List<String> eventList=new ArrayList<>();


        //add eventNames to the list
        for(int i=0;i<eventNames.length;i++) {
            eventList.add(eventNames[i]);
            Log.d("Event Names:", eventList.get(i));
        }

        ArrayAdapter tasksAdapter= new ArrayAdapter<String>(TaskFragActivity.getApplicationContext(), android.R.layout.simple_expandable_list_item_1,eventList) ;
        viewListTask.setAdapter(tasksAdapter);
        viewListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                //TODO FILL THIS IN WITH REAL task DATA
                SharedPreferences.Editor editor = TaskFragActivity.getSharedPreferences("Data", 0x0000).edit();
                //TODO NOTICE THAT WE START COUNTING FROM 1 NOT 0
                editor.putInt("eventID", 1);
                editor.putInt("position", position);
                editor.commit();
                Intent intent = new Intent(TaskFragActivity.getApplicationContext(), ViewSingleTask.class);
                startActivity(intent);
            }
        });
        tasksAdapter.notifyDataSetChanged();
        Button BackToGroupList= (Button) TaskTabRelLayout.findViewById(R.id.btnAllTaskBack);

        BackToGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskFragActivity.getApplicationContext(), GroupList.class);
                startActivity(intent);
            }
        });

        Button AddTaskButton= (Button) TaskTabRelLayout.findViewById(R.id.btnAllTaskAdd);

        AddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CreateTask.class);
                startActivity(intent);
            }
        });
        return TaskTabRelLayout;
    }

}

