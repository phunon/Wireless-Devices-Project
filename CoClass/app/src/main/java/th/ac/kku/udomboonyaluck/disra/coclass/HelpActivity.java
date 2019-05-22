package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    TextView help1,help2,help3;
    TextView txt_help1,txt_help2,txt_help3;
    ImageView arrow1,arrow2,arrow3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        txt_help1 = findViewById(R.id.txt_help1);
        txt_help2 = findViewById(R.id.txt_help2);
        txt_help3 = findViewById(R.id.txt_help3);
        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);
        txt_help1.setVisibility(View.GONE);
        txt_help2.setVisibility(View.GONE);
        txt_help3.setVisibility(View.GONE);
        help1.setOnClickListener(this);
        help2.setOnClickListener(this);
        help3.setOnClickListener(this);
    }

    public void expand(Context context, View v){
        Animation a = AnimationUtils.loadAnimation(context, R.anim.expand);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help1:
                if(txt_help1.isShown()){
                    expand(this, txt_help1);
                    txt_help1.setVisibility(View.GONE);
                    arrow1.setBackground(getResources().getDrawable(R.drawable.down));
                }
                else{
                    txt_help1.setVisibility(View.VISIBLE);
                    arrow1.setBackground(getResources().getDrawable(R.drawable.up));
                }
                break;
            case R.id.help2:
                if(txt_help2.isShown()){
                    expand(this, txt_help2);
                    txt_help2.setVisibility(View.GONE);
                    arrow2.setBackground(getResources().getDrawable(R.drawable.down));
                }
                else{
                    txt_help2.setVisibility(View.VISIBLE);
                    arrow2.setBackground(getResources().getDrawable(R.drawable.up));
                }
                break;
            case R.id.help3:
                if(txt_help3.isShown()){
                    expand(this, txt_help3);
                    txt_help3.setVisibility(View.GONE);
                    arrow3.setBackground(getResources().getDrawable(R.drawable.down));
                }
                else{
                    txt_help3.setVisibility(View.VISIBLE);
                    arrow3.setBackground(getResources().getDrawable(R.drawable.up));
                }
                break;
        }
    }
}
