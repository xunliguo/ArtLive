package com.example.rh.artlive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.NewFriendsMsgAdapter;
import com.example.rh.artlive.db.InviteMessage;
import com.example.rh.artlive.db.InviteMessgeDao;
import com.example.rh.artlive.util.Log;

import java.util.List;


/**
 * 申请与通知
 *
 */
public class NewFriendsMsgActivity extends Activity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);

		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		Log.e("消息"+dao);
		List<InviteMessage> msgs = dao.getMessagesList();
		Log.e("消息"+msgs);
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
		
	}

	public void back(View view) {
		finish();
	}
}
