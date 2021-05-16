package classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapterForExam extends RecyclerView.Adapter<QuestionAdapterForExam.ViewHolder> {

    ArrayList<Question> arrayList;
    List<Integer> selectedQuestions = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    DBHelper database;

    public QuestionAdapterForExam(ArrayList<Question> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.database = new DBHelper(context);
    }

    // Her bir satır için temsil edilecek olan arayüz belirlenir.
    @NonNull
    @Override
    public QuestionAdapterForExam.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.question_item,parent,false);
        return new QuestionAdapterForExam.ViewHolder(view);
    }

    // Her bir satırın içeriği belirlenir.
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapterForExam.ViewHolder holder, int position) {
        holder.question_txt.setText(arrayList.get(position).getQuestionText());
        holder.question_img.setImageResource(R.drawable.question_mark);
        holder.ll_list_element.setTag(holder);
        // Listedeki elemanlara tıklanıdığında yapılacak olan işlem...
        holder.ll_list_element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionAdapterForExam.ViewHolder holder1 = (QuestionAdapterForExam.ViewHolder)view.getTag();
                int position = holder1.getAdapterPosition();
                int qID = arrayList.get(position).getqID();
                if(!selectedQuestions.contains(qID)){
                    selectedQuestions.add(qID);
                    holder1.ll_list_element.setBackgroundColor(Color.parseColor("#72B65C"));
                }
                else{
                    selectedQuestions.removeIf(s -> s.equals(qID));
                    holder1.ll_list_element.setBackgroundColor(Color.parseColor("#BB86FC"));
                }
                Intent intent = new Intent("selected_qID");
                intent.putExtra("question_id", qID);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }

    // Listedeki eleman sayısı kadar işlemin yapılmasını sağladık. Elle de bir değer verilebilirdi.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Elemanlarımıza erişip tanımladığımız yer
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView question_txt;
        ImageView question_img;
        LinearLayout ll_list_element;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question_txt = itemView.findViewById(R.id.question_preview);
            question_img = itemView.findViewById(R.id.img_question);
            ll_list_element = itemView.findViewById(R.id.ll_list_element);
        }
    }
}
