package com.mai.nix.maiapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class ChooseGroupActivity extends AppCompatActivity{
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mGroups;
    private Spinner mSpinnerFacs, mSpinnerStages;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mCurrentGroup;
    public static final String EXTRA_GROUP = "com.mai.nix.group_result";
    private static final String MODE = "com.mai.nix.maiapp.mode";
    private String[] FAC_CODES = {"150", "153", "157", "149", "155", "160", "154", "151",
            "152", "146", "165", "164", "168", "169"};
    private final String LINK = "http://mai.ru/education/schedule/?department=";
    private final String PLUS_COURSE = "&course=";
    private int mCurrentFac = -1, mCurrentStage = -1;
    private boolean isForSettings;

    public static Intent newIntent(Context context, boolean mode){
        Intent intent = new Intent(context, ChooseGroupActivity.class);
        intent.putExtra(MODE, mode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        mGroups = new ArrayList<>();
        UserSettings.initialize(this);
        isForSettings = getIntent().getBooleanExtra(MODE, false);
        String[] mFacsArray = getResources().getStringArray(R.array.spinner_facs);
        String[] mStagesArray = getResources().getStringArray(R.array.spinner_stages);
        View header = View.inflate(this, R.layout.group_choosing_header, null);
        mSpinnerFacs = header.findViewById(R.id.spinner_facs);
        mSpinnerStages = header.findViewById(R.id.spinner_stages);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_groups);
        HintAdapter hintAdapter = new HintAdapter(this, android.R.layout.simple_spinner_item, mFacsArray);
        hintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        HintAdapter hintAdapter2 = new HintAdapter(this, android.R.layout.simple_spinner_item, mStagesArray);
        hintAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerFacs.setAdapter(hintAdapter);
        mSpinnerStages.setAdapter(hintAdapter2);
        mSpinnerFacs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    mCurrentFac = i-1;
                }
                if(mCurrentStage != -1){
                    new MyThread().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
        mSpinnerStages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    mCurrentStage = i;
                }
                if(mCurrentFac != -1){
                    new MyThread().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
        mListView = findViewById(R.id.listview);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, mGroups);
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentGroup =  mGroups.get(i-1);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mCurrentStage != -1 && mCurrentFac != -1) {
                    new MyThread().execute();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private class MyThread extends AsyncTask<Integer, Void, Integer>{
        private Document doc;
        private Elements titles;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Integer s) {
            mSwipeRefreshLayout.setRefreshing(false);
            if(s == 0){
                Toast.makeText(ChooseGroupActivity.this, R.string.error,
                        Toast.LENGTH_LONG).show();
            }else{
                mListView.setAdapter(mAdapter);
            }
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            try {
                String link = LINK.concat(FAC_CODES[mCurrentFac]).concat(PLUS_COURSE)
                        .concat(Integer.toString(mCurrentStage));
                Log.d("ChooseGroupLink ", link);
                doc = Jsoup.connect(link).get();
                titles = doc.select("a[class = sc-group-item]");
                if(titles == null) return 0;
                if (titles.size() > 0){
                    mGroups.clear();
                    for(int i = 0; i < titles.size(); i++){
                        mGroups.add(titles.get(i).text());
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            if (titles != null) return titles.size(); else return 0;
        }
    }

    private class HintAdapter extends ArrayAdapter<String>{
        Context mContext;
        public HintAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
            super(context, resource, objects);
            this.mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            TextView texview = (TextView) view.findViewById(android.R.id.text1);

            if(position == 0) {
                texview.setText("");
                texview.setHint(getItem(position)); //"Hint to be displayed"
            } else {
                texview.setText(getItem(position));
            }

            return view;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view;

            if(position == 0){
                view = inflater.inflate(R.layout.spinner_hint_list_item_layout, parent, false); // Hide first row
            } else {
                view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                TextView texview = (TextView) view.findViewById(android.R.id.text1);
                texview.setText(getItem(position));
            }
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_groups_go, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mCurrentGroup != null){
            if(isForSettings){
                UserSettings.setGroup(this, mCurrentGroup);
                Intent i = new Intent(ChooseGroupActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                setGroupResult(mCurrentGroup);
                this.finish();
            }
        }else{
            Toast.makeText(this, R.string.exception_group_null, Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
    private void setGroupResult(String group){
        Intent data = new Intent();
        data.putExtra(EXTRA_GROUP, group);
        setResult(RESULT_OK, data);
    }
}