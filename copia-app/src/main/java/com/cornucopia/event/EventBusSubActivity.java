package com.cornucopia.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cornucopia.R;

import org.greenrobot.eventbus.EventBus;

public class EventBusSubActivity extends Activity implements OnClickListener {

    private Button mBtnEvent;

    private Button mBtnEventSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eventbus);

        mBtnEvent = (Button) findViewById(R.id.btn_eventbus);
        mBtnEventSub = (Button) findViewById(R.id.btn_eventbus_sub);

        mBtnEvent.setOnClickListener(this);
        mBtnEventSub.setOnClickListener(this);

        mBtnEventSub.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_eventbus) {
                EventBus.getDefault().postSticky(new MessageEvent("sub event message click"));
            }
        if (v.getId() == R.id.btn_eventbus_sub) {
                Intent intent = new Intent(this, EventBusSubActivity.class);
                startActivity(intent);
        }
    }
}
