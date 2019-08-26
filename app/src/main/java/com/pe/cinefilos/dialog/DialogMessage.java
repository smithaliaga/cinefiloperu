package proguide.walleton.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import proguide.walleton.R;

/**
 * Created by Epica on 5/5/2017.
 */

public class DialogMessage extends Dialog implements View.OnClickListener {
    private TextView txt_message;
    public DialogMessage(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_messages);
        this.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txt_message = (TextView)findViewById(R.id.dialog_message);
        findViewById( R.id.button_accept_message ).setOnClickListener( this );
    }


    public void setMessage(String msg){
        txt_message.setText(msg);
    }
    @Override
    public void onClick(View view) {
        switch ( view.getId( ) )
        {
            case R.id. button_accept_message:
                dismiss();
                break;
        }
    }
}
