package edu.mtu.sercsoundsampler

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText
import android.widget.LinearLayout;
import android.widget.Toast

import androidx.appcompat.app.AlertDialog;
import edu.mtu.sercsoundsampler.model.SourceListKeeper

class SourceInputDialog(val activity: Activity, val keeper: SourceListKeeper) {
    val context = activity.applicationContext
    val title = context.resources.getString(R.string.addSound)
    val prompt = context.resources.getString(R.string.prompt_for_name)
    val ok = context.resources.getString(R.string.ok)
    val cancel = context.resources.getString(R.string.cancel)
    val already = context.resources.getString(R.string.soundAlreadyAdded)

    fun show() {
        val rv = AlertDialog.Builder(activity)
        val input = EditText(activity)
        val inputLayout = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
            , LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = inputLayout
        rv.setView(input)
                .setTitle(title)
                .setMessage(prompt)
                .setNegativeButton(cancel, { d , i -> {}} )
                .setPositiveButton(ok, { d, i -> add(input) } )
                .show()
    }

    fun add(i: EditText) {
        val s = i.text.toString().trim();
        if (keeper.contains(s)) {
            Toast.makeText(context, already, Toast.LENGTH_SHORT).show()
            i.setText(R.string.empty)
        } else if (s.length > 0) keeper.add(s)
    }

    /*
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    alertDialog.setTitle("PASSWORD");
    alertDialog.setMessage("Enter Password");

    final EditText input = new EditText(MainActivity.this);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
    LinearLayout.LayoutParams.MATCH_PARENT,
    LinearLayout.LayoutParams.MATCH_PARENT);
    input.setLayoutParams(lp);
    alertDialog.setView(input);
    alertDialog.setIcon(R.drawable.key);

    alertDialog.setPositiveButton("YES",
    new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            password = input.getText().toString();
            if (password.compareTo("") == 0) {
                if (pass.equals(password)) {
                    Toast.makeText(getApplicationContext(),
                        "Password Matched", Toast.LENGTH_SHORT).show();
                    Intent myIntent1 = new Intent(view.getContext(),
                        Show.class);
                    startActivityForResult(myIntent1, 0);
                } else {
                    Toast.makeText(getApplicationContext(),
                        "Wrong Password!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    alertDialog.setNegativeButton("NO",
    new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

    alertDialog.show();
}

});

     */
}