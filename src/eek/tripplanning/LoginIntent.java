package eek.tripplanning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginIntent extends Activity {

	EditText txtName;
	EditText txtPassword;
	Button btnLoginHome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		txtName=(EditText) findViewById(R.id.txtName);
		txtPassword=(EditText) findViewById(R.id.txtPassword);
		btnLoginHome=(Button) findViewById(R.id.btnLoginHome);
		
		btnLoginHome.setOnClickListener(new onClick());
		}
	
	public class onClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				if(v.getId()==btnLoginHome.getId()){
					String name,password;
					name=txtName.getText().toString();
					password=txtPassword.getText().toString();
					if(name.equals("admin")&&(password.equals("admin"))){
							Intent adminIntent=new Intent(LoginIntent.this, AdminHomeIntent.class);
							startActivity(adminIntent);
					}
				}
		}

	}
	}


