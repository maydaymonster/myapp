package com.ttit.myapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ttit.myapp.PlanDatabase.CRUD;
import com.ttit.myapp.PlanDatabase.Note;
import com.ttit.myapp.PlanDatabase.NoteAdapter;
import com.ttit.myapp.PlanDatabase.NoteDatabase;
import com.ttit.myapp.R;
import com.ttit.myapp.activity.EditActivity;
import com.ttit.myapp.alarm.PlanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

import static android.content.ContentValues.TAG;

public class NewsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    ListView lv;
    Toolbar planbar;
    FloatingActionButton btn;
    TextView tv;
    private ListView v;
    private NoteAdapter adapter;
    private NoteDatabase dbHelper;
    private List<Note> noteList=new ArrayList<Note>();
    public NewsFragment() {

    }
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }
    @Override
    protected int initLayout() {
        return R.layout.fragment_news;

    }

    @Override
    protected void initView() {
        setHasOptionsMenu(true);
        lv=mRootView.findViewById(R.id.lv);
        planbar=mRootView.findViewById(R.id.planbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(planbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toobar代替actionbar
        planbar.setNavigationIcon(R.drawable.ic_menuplan);
        planbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),PlanActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        adapter=new NoteAdapter(getContext(),noteList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        refreshListView();

    }

    @OnClick({R.id.fab})
    public void onViewClicked(View view) {
        Intent intent=new Intent(getActivity(),EditActivity.class);
        intent.putExtra("mode",4);
        startActivityForResult(intent,0);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int returnMode;
        long note_Id;
        returnMode = data.getExtras().getInt("mode", 0);
        note_Id = data.getExtras().getLong("id", 0);
        if (returnMode == 1) {  //update current note
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(content, time, tag);
            newNote.setId(note_Id);
            CRUD op = new CRUD(getActivity());
            op.open();
            op.updateNote(newNote);
            op.close();
        }
        else if (returnMode == 0) {  // create new note
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(content, time, tag);
            CRUD op = new CRUD(getActivity());
            op.open();
            op.addNote(newNote);
            op.close();
        }else if(returnMode == 2) { // delete
            Note curNote = new Note();
            curNote.setId(note_Id);
            CRUD op = new CRUD(getActivity());
            op.open();
            op.removeNote(curNote);
            op.close();
        }else{
        }
        refreshListView();
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mainplan_menu, menu);
        MenuItem mSearch=menu.findItem(R.id.action_search);
        SearchView mSearchView=(SearchView)mSearch.getActionView();
        mSearchView.setQueryHint("查找你的计划");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void refreshListView(){
        CRUD op = new CRUD(getActivity());
        op.open();
        // set adapter
        if (noteList.size() > 0) noteList.clear();
        noteList.addAll(op.getAllNotes());
        op.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_clear:
                new AlertDialog.Builder(getActivity())
                        .setMessage("删除全部吗")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper=new NoteDatabase(getContext());
                                SQLiteDatabase db=dbHelper.getWritableDatabase();
                                db.delete("notes",null,null);
                                db.execSQL("update sqlite_sequence set seq=0 where name='notes'");
                                refreshListView();

                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv:
                Note curNote = (Note) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("mode", 3);     // MODE of 'click to edit'
                intent.putExtra("tag", curNote.getTag());
                startActivityForResult(intent, 1);
                Log.d(TAG, "onItemClick: "+position);
                break;
        }
    }}
