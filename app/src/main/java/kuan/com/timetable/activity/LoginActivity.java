package kuan.com.timetable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import kuan.com.timetable.PanelActivity;
import kuan.com.timetable.R;
import kuan.com.timetable.base.BaseActivity;

/**
 * Created by kys-34 on 2016/12/2 0002.
 */

public class LoginActivity extends BaseActivity {
    private EditText login_name, login_passwd;
    private Button login_commit, login_regist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        login_name = (EditText) findViewById(R.id.login_name);
        login_passwd = (EditText) findViewById(R.id.login_passwd);
        login_commit = (Button) findViewById(R.id.login_commit);
        login_regist = (Button) findViewById(R.id.login_regist);
    }

    @Override
    public void initData() {
        super.initData();
        login_commit.setOnClickListener(new MyClick());
        login_regist.setOnClickListener(new MyClick());

    }

    private class MyClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            BmobUser bmobUser = new BmobUser();
            switch (view.getId()) {
                case R.id.login_commit:
                    BmobUser.loginByAccount(login_name.getText().toString(), login_passwd.getText().toString(), new LogInListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (bmobUser!=null){
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, PanelActivity.class));
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this,"登陆失败\n原因是\n"+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case R.id.login_regist:
                    bmobUser.setUsername(login_name.getText().toString());
                    bmobUser.setPassword(login_passwd.getText().toString());
                    bmobUser.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                            } else {
                                Toast.makeText(LoginActivity.this, "注册失败\n" + "原因是\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;

            }
        }
//        public void permission(){
//            final PermissionManager helper=null;
//            helper = PermissionManager.with(LoginActivity.this)
//                    //添加权限请求码
//                    .addRequestCode(MainActivity.REQUEST_CODE_CAMERA)
//                    //设置权限，可以添加多个权限
//                    .permissions(Manifest.permission.CAMERA)
//                    //设置权限监听器
//                    .setPermissionsListener(new PermissionListener() {
//
//                        @Override
//                        public void onGranted() {
//                            //当权限被授予时调用
//                            Toast.makeText(LoginActivity.this, "Camera Permission granted",Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onDenied() {
//                            //用户拒绝该权限时调用
//                            Toast.makeText(LoginActivity.this, "Camera Permission denied",Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onShowRationale(String[] permissions) {
//                            //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
//                            Snackbar.make(btn_camera, "需要相机权限去拍照", Snackbar.LENGTH_INDEFINITE)
//                                    .setAction("ok", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            //必须调用该`setIsPositive(true)`方法
//                                            helper.setIsPositive(true);
//                                            helper.request();
//                                        }
//                                    }).show();
//                        }
//                    })
//                    //请求权限
//                    .request();
//        }
    }

}
