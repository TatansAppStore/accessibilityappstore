package net.accessiblility.app.store.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import net.accessiblility.app.store.R;

/**
 * Created by Administrator on 2016/10/26.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startUpActivity();
    }

    private void startUpActivity() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);//设置电话透明度
        animation.setDuration(3000);//设置动画时间
        TextView img_logo = (TextView) this.findViewById(R.id.tv_splash);
        img_logo.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

                                           public void onAnimationEnd(Animation animation)

                                           {
                                               //动画结束后跳转到登陆界面
                                               Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                               startActivity(intent);
                                               finish();

                                           }

                                           public void onAnimationRepeat(Animation animation)

                                           {

                                               // 动画重复
                                           }

                                           public void onAnimationStart(Animation animation) {
                                           }


                                       }

        );
    }

}
