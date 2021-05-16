package classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.QuestionActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    ArrayList<Question> arrayList;
    LayoutInflater layoutInflater;
    Context context;
    DBHelper database;

    public QuestionAdapter(ArrayList<Question> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.database = new DBHelper(context);
    }

    // Her bir satır için temsil edilecek olan arayüz belirlenir.
    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.question_item,parent,false);
        return new ViewHolder(view);
    }

    // Her bir satırın içeriği belirlenir.
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        holder.question_txt.setText(arrayList.get(position).getQuestionText());
        holder.question_img.setImageResource(R.drawable.question_mark);
        holder.ll_list_element.setTag(holder);
        // Listedeki elemanlara tıklanıdığında yapılacak olan işlem...
        holder.ll_list_element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder holder1 = (ViewHolder)view.getTag();
                int position = holder1.getAdapterPosition();
                int qID = arrayList.get(position).getqID();
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("question_id", qID);
                context.startActivity(intent);
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