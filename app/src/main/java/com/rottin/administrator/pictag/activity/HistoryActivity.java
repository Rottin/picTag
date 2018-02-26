package com.rottin.administrator.pictag.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rottin.administrator.pictag.HistoryDAO;
import com.rottin.administrator.pictag.R;
import com.rottin.administrator.pictag.domain.Exercise1;
import com.rottin.administrator.pictag.domain.Exercise2;
import com.rottin.administrator.pictag.domain.Exercise3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private TextView history_return, history_submit;
    private ListView listView;
    private HistoryDAO dao;
    private List<Exercise1> exercise1s;
    private List<Exercise2> exercise2s;
    private List<Exercise3> exercise3s;
    private HistoryAdapter adapter;
    private String userName;
    private Socket socket1;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String sendstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.history_list);
        history_return = (TextView) findViewById(R.id.history_return);
        history_submit = (TextView) findViewById(R.id.history_submit);

        history_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();
            }
        });

        dao = new HistoryDAO(this);
        refreshData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTv = (TextView) view.findViewById(R.id.item_history_id_hide);
                TextView typeTv = (TextView) view.findViewById(R.id.item_history_type_hide);
                if (typeTv.getText().equals("1")) {
                    Intent intent = new Intent(HistoryActivity.this, ShowExercise1.class);
                    intent.putExtra("id", "" + idTv.getText().toString());
                    intent.putExtra("type", "" + typeTv.getText().toString());
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                    finish();
                } else if (typeTv.getText().equals("2")) {
                    Intent intent = new Intent(HistoryActivity.this, ShowExercise2.class);
                    intent.putExtra("id", "" + idTv.getText().toString());
                    intent.putExtra("type", "" + typeTv.getText().toString());
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                    finish();
                } else if (typeTv.getText().equals("3")) {
                    Intent intent = new Intent(HistoryActivity.this, ShowExercise3.class);
                    intent.putExtra("id", "" + idTv.getText().toString());
                    intent.putExtra("type", "" + typeTv.getText().toString());
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                    finish();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTv = (TextView) view.findViewById(R.id.item_history_id_hide);
                TextView typeTv = (TextView) view.findViewById(R.id.item_history_type_hide);
                dialog("" + idTv.getText().toString(), "" + typeTv.getText().toString());
                return true;
            }
        });
    }

    private void dialog(final String id, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setMessage("确定删除这条历史记录？");
        builder.setTitle("删除");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.deleteById(id, type);
                dialog.dismiss();
                refreshData();
                Toast.makeText(HistoryActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void refreshData() {
        userName = getIntent().getStringExtra("username");
        exercise1s = dao.findAll1(userName);
        exercise2s = dao.findAll2(userName);
        exercise3s = dao.findAll3(userName);
        history_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSubmit();
            }
        });
        if (adapter == null) {
            adapter = new HistoryAdapter();
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private class HistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (exercise1s != null)
                count += exercise1s.size();
            if (exercise2s != null)
                count += exercise2s.size();
            if (exercise3s != null)
                count += exercise3s.size();
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = View.inflate(HistoryActivity.this, R.layout.item_history, null);

            } else {
                view = convertView;
            }
            TextView summaryTv = (TextView) view.findViewById(R.id.item_history_summary);
            TextView timeTv = (TextView) view.findViewById(R.id.item_history_time);
            TextView hide_type = (TextView) view.findViewById(R.id.item_history_type_hide);
            TextView hide_id = (TextView) view.findViewById(R.id.item_history_id_hide);

            if (position < exercise1s.size()) {
                Exercise1 exercise1 = exercise1s.get(position);
                String summary = exercise1.getT1();
                if (exercise1.getT2() != null && (!exercise1.getT2().equals(""))) {
                    summary += ", " + exercise1.getT2();
                }
                if (exercise1.getT3() != null && (!exercise1.getT3().equals(""))) {
                    summary += ", " + exercise1.getT3();
                }
                if (exercise1.getT4() != null && (!exercise1.getT4().equals(""))) {
                    summary += ", " + exercise1.getT4();
                }
                summaryTv.setText("题型一：" + summary);
                timeTv.setText(exercise1.getTime());
                hide_type.setText("1");
                hide_id.setText("" + exercise1.getId());
            } else if (position >= exercise1s.size() && position < exercise1s.size() + exercise2s.size()) {
                Exercise2 exercise2 = exercise2s.get(position - exercise1s.size());
                String summary = exercise2.getT1();
                if (exercise2.getT2() != null && (!exercise2.getT2().equals(""))) {
                    summary += ", " + exercise2.getT2();
                }
                if (exercise2.getT3() != null && (!exercise2.getT3().equals(""))) {
                    summary += ", " + exercise2.getT3();
                }
                if (exercise2.getT4() != null && (!exercise2.getT4().equals(""))) {
                    summary += ", " + exercise2.getT4();
                }
                summaryTv.setText("题型二：" + summary);
                timeTv.setText(exercise2.getTime());
                hide_type.setText("2");
                hide_id.setText("" + exercise2.getId());
            } else if (position >= exercise1s.size() + exercise2s.size()) {
                Exercise3 exercise3 = exercise3s.get(position - exercise1s.size() - exercise2s.size());
                String summary = exercise3.getT();
                summaryTv.setText("题型三：" + summary);
                timeTv.setText(exercise3.getTime());
                hide_type.setText("3");
                hide_id.setText("" + exercise3.getId());
            }
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private void finalSubmit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setMessage("确定最终提交？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            socket1 = new Socket("120.25.76.27", 1222);
                            writer = new BufferedWriter(
                                    new OutputStreamWriter(socket1.getOutputStream()));
                            sendstr = "[Submit]" + userName;
                            for (int i = 0; i < exercise1s.size(); i++) {
                                //int count = 0;


                                if (exercise1s.get(i).getT1() != null && (!exercise1s.get(i).getT1().equals("")))
                                    sendstr += "," + exercise1s.get(i).getPicid() + "," + exercise1s.get(i).getT1();
                                if (exercise1s.get(i).getT2() != null && (!exercise1s.get(i).getT2().equals("")))
                                    sendstr += "," + exercise1s.get(i).getPicid() + "," + exercise1s.get(i).getT2();
                                if (exercise1s.get(i).getT3() != null && (!exercise1s.get(i).getT3().equals("")))
                                    sendstr += "," + exercise1s.get(i).getPicid() + "," + exercise1s.get(i).getT3();
                                if (exercise1s.get(i).getT4() != null && (!exercise1s.get(i).getT4().equals("")))
                                    sendstr += "," + exercise1s.get(i).getPicid() + "," + exercise1s.get(i).getT4();

                                Log.i("sendstr", sendstr);

                            }
                            for (int j = 0; j < exercise2s.size(); j++) {


                                if (exercise2s.get(j).getSelected().charAt(0) == '1')
                                    sendstr += "," + exercise2s.get(j).getPicid() + "," + exercise2s.get(j).getT1();
                                if (exercise2s.get(j).getSelected().charAt(1) == '1')
                                    sendstr += "," + exercise2s.get(j).getPicid() + "," + exercise2s.get(j).getT2();
                                if (exercise2s.get(j).getSelected().charAt(2) == '1')
                                    sendstr += "," + exercise2s.get(j).getPicid() + "," + exercise2s.get(j).getT3();
                                if (exercise2s.get(j).getSelected().charAt(3) == '1')
                                    sendstr += "," + exercise2s.get(j).getPicid() + "," + exercise2s.get(j).getT4();

                                Log.i("sendstr", sendstr);
                            }
                            for (int j = 0; j < exercise3s.size(); j++) {

                                if (exercise3s.get(j).getSelected().charAt(0) == '1')
                                    sendstr += "," + exercise3s.get(j).getPicid1() + "," + exercise3s.get(j).getT();
                                if (exercise3s.get(j).getSelected().charAt(1) == '1')
                                    sendstr += "," + exercise3s.get(j).getPicid2() + "," + exercise3s.get(j).getT();
                                if (exercise3s.get(j).getSelected().charAt(2) == '1')
                                    sendstr += "," + exercise3s.get(j).getPicid3() + "," + exercise3s.get(j).getT();
                                if (exercise3s.get(j).getSelected().charAt(3) == '1')
                                    sendstr += "," + exercise3s.get(j).getPicid4() + "," + exercise3s.get(j).getT();

                                Log.i("sendstr", sendstr);
                            }
                            sendstr += "\n";
                            writer.write(sendstr);
                            writer.flush();
                            //edit.setText("");
                            writer.close();
                            publishProgress();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        //清空数据库
                        dao.deleteAll();
                        refreshData();

                        super.onProgressUpdate(values);
                    }
                };
                asyncTask.execute();
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
