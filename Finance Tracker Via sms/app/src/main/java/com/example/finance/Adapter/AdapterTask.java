package com.example.finance.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.Models.ModelTask;
import com.example.finance.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterTask extends FirebaseRecyclerAdapter<ModelTask, AdapterTask.myviewHolder> {

    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    List<ModelTask>taskList;
    private Context context;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *  @param options
     *
     */
    public AdapterTask(@NonNull FirebaseRecyclerOptions<ModelTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder,final int position, @NonNull ModelTask model) {
        holder.day.setText(model.getDay());
        holder.mon.setText(model.getMonth());
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDesc());
        holder.status.setText(model.getStatus());
        holder.date.setText(model.getDate());
        holder.option.setOnClickListener(view -> showpopupmenu(view, position));
    }


    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new myviewHolder(view);

    }

    class myviewHolder extends RecyclerView.ViewHolder {
        TextView day, date, mon, title, description, status;
        ImageView option;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            mon = itemView.findViewById(R.id.mon);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            status = itemView.findViewById(R.id.status);
            option = itemView.findViewById(R.id.options);
        }
    }


    private void showpopupmenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.taskmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation);
                    alertDialogBuilder.setMessage(R.string.sureToDelete);
                    alertDialogBuilder.setPositiveButton("yes", (dialog, which) -> {
                        mdatabase = FirebaseDatabase.getInstance().getReference().child("reminders").child(user.getUid());
                        mdatabase.removeValue();

                    });
                    alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    alertDialogBuilder.show();
                    break;
                case R.id.menuComplete:
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
                    completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                            setPositiveButton("Yes", (dialog, which) ->showCompleteDialog(view))
                            .setNegativeButton("No", (dialog, which) -> dialog.cancel()).show();
                    break;
            }
            return false;
        });
        popupMenu.show();
    }
    public void showCompleteDialog(View view) {
        Dialog dialog = new Dialog(view.getContext(), R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_completed_theme);
        Button close = dialog.findViewById(R.id.closeButton);
        close.setOnClickListener(view1 -> {
            mdatabase = FirebaseDatabase.getInstance().getReference().child("reminders").child(user.getUid());
            mdatabase.removeValue();

            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

}