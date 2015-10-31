package me.liuyichen.demo.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.liuyichen.viewinjector.ViewInjector;
import me.liuyichen.viewinjector.annotation.InjectView;

public class OtherActivity extends AppCompatActivity {

    public static void launch(Context context) {

        context.startActivity(new Intent(context, OtherActivity.class));
    }

    @InjectView(R.id.recyclerview)
    public RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ViewInjector.inject(this, this);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new MyAdapter());
    }

    public static class MyAdapter extends RecyclerView.Adapter{

        private static String[] LIST = {"item1"
                ,"item2"
                ,"item3"
                ,"item4"
                ,"item5"
                ,"item6"
                ,"item7"
                ,"item8"
                ,"item9"
        };

        public MyAdapter() {
            super();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyViewHolder viewHolder = (MyViewHolder)holder;

            viewHolder.tv.setText(LIST[position]);
        }

        @Override
        public int getItemCount() {
            return LIST.length;
        }


        static class MyViewHolder extends RecyclerView.ViewHolder{

            @InjectView(android.R.id.text1)
            public TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                ViewInjector.inject(this, itemView);
            }
        }
    }
}
