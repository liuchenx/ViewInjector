package me.liuyichen.demo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.liuyichen.viewinjector.ViewInjector;
import me.liuyichen.viewinjector.annotation.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.textview)
    public TextView tv;

    @InjectView(R.id.button)
    public Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.inject(this, this);

        tv.setText("Hello, InjectView");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OtherActivity.launch(MainActivity.this);
            }
        });
    }
}
