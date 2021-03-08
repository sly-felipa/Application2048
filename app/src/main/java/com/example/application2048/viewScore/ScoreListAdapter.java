package com.example.application2048.viewScore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application2048.R;
import com.example.application2048.activity.ManageScoresActivity;
import com.example.application2048.eventlisteners.ButtonOnClickListener;
import com.example.application2048.eventlisteners.OnEditingEventListener;
import com.example.application2048.eventlisteners.OnScoreDeletingEventListener;
import com.example.application2048.eventlisteners.OnSharingEventListener;
import com.example.application2048.model.Score;

import java.util.ArrayList;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {

    class ScoreViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textDate;
        private TextView textPoints;
        private TextView textTime;
        public final Button delete_button;
        public final Button share_button;
        public final Button edit_button;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txtUserName);
            textDate = itemView.findViewById(R.id.txtDate);
            textPoints = itemView.findViewById(R.id.txtPoints);
            textTime = itemView.findViewById(R.id.txtTime);
            delete_button = itemView.findViewById(R.id.btnDelete);
            share_button = itemView.findViewById(R.id.btnShare);
            edit_button = itemView.findViewById(R.id.btnEdit);
        }
    }

    private final LayoutInflater mInflater;
    private ArrayList<Score> scoreList;
    static ManageScoresActivity mContext;
    private OnScoreDeletingEventListener mOnScoreDeleting;
    private OnSharingEventListener mOnSharingRequest;
    private OnEditingEventListener mOnScoreEditing;

    public ScoreListAdapter(Context context, ArrayList<Score> scoreListOrigin) {
        mInflater = LayoutInflater.from(context);
        mContext = (ManageScoresActivity) context;
        scoreList = scoreListOrigin;
    }

    /**
     * Setea la lista de scores a mostrar.
     *
     * @param scores
     */
    public void setScoreList(ArrayList<Score> scores) {
        this.scoreList = scores;
        this.notifyDataSetChanged();
    }

    /**
     * Setea un evento para luego borrar un score.
     *
     * @param eventListener
     */
    public void setOnScoreDeletingEventListener(OnScoreDeletingEventListener eventListener) {
        mOnScoreDeleting = eventListener;
    }

    /**
     * Setea un evento para luego compartir un score.
     *
     * @param eventListener
     */
    public void setOnSharingEventListener(OnSharingEventListener eventListener) {
        mOnSharingRequest = eventListener;
    }

    /**
     * Setea un evento para luego editar un score.
     *
     * @param eventListener
     */
    public void setOnScoreEditingEventListener(OnEditingEventListener eventListener) {
        mOnScoreEditing = eventListener;
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.scorelist_item, parent, false);
        return new ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        final Score current = scoreList.get(position);
        holder.textName.setText(String.valueOf(current.getName()));
        holder.textPoints.setText(String.valueOf(current.getPoints()));
        holder.textDate.setText(current.getFormattedDate());
        holder.textTime.setText(current.getFormattedSecondsGame());

        final ScoreViewHolder viewHolder = holder;
        holder.delete_button.setOnClickListener(new ButtonOnClickListener(
                (int) current.getId()) {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = dialogConfirmation(viewHolder, current);
                builder.create();
                builder.show();
            }
        });

        holder.share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String body = current.getName() + ";" + current.getPoints() + ";" + current.getFormattedSecondsGame();

                if (mOnSharingRequest != null) {
                    mOnSharingRequest.onSharingRequest("Te comparto mi puntuación!", body);
                }
            }
        });

        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnScoreEditing != null) {
                    mOnScoreEditing.onScoreEdit(current);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    /**
     * Diálogo de confirmación antes de borrar un score.
     *
     * @param holder
     * @param score
     * @return
     */
    public AlertDialog.Builder dialogConfirmation(final ScoreViewHolder holder, final Score score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.confirm_dialog_message)
                .setTitle(R.string.confirm_dialog_title)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mOnScoreDeleting != null) {
                            mOnScoreDeleting.onScoreDelete(score);
                        }
                        scoreList.remove(score);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext, R.string.descriptionScore, Toast.LENGTH_LONG).show();
//                        if (mOnAdapterLoaded != null) {
//                            mOnAdapterLoaded.onLoaded("Hola");
//                        }

                    }
                });
        return builder;
    }
}
