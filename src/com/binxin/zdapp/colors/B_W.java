package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;

public class B_W extends Activity
{
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.xml.colors_b_w);

		Button next = (Button) findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent next = new Intent(B_W.this,W_B.class);
				startActivity(next);
				finish();
			}
		});
	}
}
