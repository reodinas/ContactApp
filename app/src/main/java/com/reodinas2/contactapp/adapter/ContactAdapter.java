package com.reodinas2.contactapp.adapter;


// 1. RecyclerView.Adapter를 상속 받는다.

// 2. 상속받은 클래스가 abstract 이므로, unimplemented method를 구현해야 한다.

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.reodinas2.contactapp.EditActivity;
import com.reodinas2.contactapp.MainActivity;
import com.reodinas2.contactapp.R;
import com.reodinas2.contactapp.data.DatabaseHandler;
import com.reodinas2.contactapp.model.Contact;

import java.io.Serializable;
import java.util.List;


// 6. extends 부분에 RecyclerView.Adapter의 데이터 타입을 적어주어야 한다.
//    우리가 만든 ViewHolder로 적는다.( <ContactAdapter.ViewHolder> )

// 7. 빨간색 에러가 뜨면,
//    onCreateViewHolder, onBindViewHolder 메소드에 있는 RecyclerView.ViewHolder를
//    우리가 만든 ViewHolder로 바꿔준다.(ContactAdapter.ViewHolder)
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    // 5. 어댑터 클래스의 멤버변수와 생성자를 만들어 준다.
    Context context;
    List<Contact> contactList;

    // 유저가 삭제를 누른 인덱스
    int deleteIndex;



    // 이건 디폴트 생성자를 만들지 않는다.
    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    // 8. 오버라이드 함수 3개를 전부 구현해주면 끝!
    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        // 뷰에 데이터를 셋팅한다.
        Contact contact = contactList.get(position);

        holder.txtName.setText(contact.name);
        holder.txtPhone.setText(contact.phone);
    }

    @Override
    public int getItemCount() {
        // 전체 데이터의 갯수를 적어준다.
        return contactList.size();
    }



    // 3. 어댑터 클래스 내에 ViewHolder 클래스를 만든다.
    //    이 클래스는 contact_row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    //    RecyclerView.ViewHolder를 상속받는다

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtPhone;
        ImageView imgDelete;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 1. 인텐트에 유저가 누른 행의 이름과 전화번호를 담아서
                    int index = getAdapterPosition();  // 누른 행의 인덱스를 가져오는 메소드

                    Contact contact = contactList.get(index);

                    // 2. 수정하는 액티비티를 띄운다.
                    // 어떤 액티비티가 어떤 액티비티를 띄운다! => intent에 있어야 한다.
                    Intent intent = new Intent(context, EditActivity.class);

//                    intent.putExtra("id", contact.id);
//                    intent.putExtra("name", contact.name);
//                    intent.putExtra("phone", contact.phone);
                    intent.putExtra("contact", contact);

                    context.startActivity(intent);

                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 1. 어느 주소록을 삭제할 것인지,
                    //    삭제할 주소록을 가져온다.
                    deleteIndex = getAdapterPosition();

                    // 2. 알러트 다이얼로그가 나온다.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("주소록 삭제");
                    builder.setMessage("정말 삭제하시겠습니까?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 3. 알러트 다이얼로그에서 Yes 눌렀을 때
                            //    => 데이터베이스에서 삭제
                            DatabaseHandler db = new DatabaseHandler(context);

                            Contact contact = contactList.get(deleteIndex);

                            db.deleteContact(contact);

                            // 알러트 다이얼로그는 액티비티가 아니므로
                            // 메인액티비티의 onResume 함수가 실행 안된다.
                            // 따라서 화면갱신이 안된다.

                            // 즉, 디비에 저장된 데이터를 삭제했으니,
                            // 메모리에 저장된 데이터도 삭제한다.
                            contactList.remove(deleteIndex);
                            // 데이터가 변경되었으니, 화면 갱신하라고 함수 호출
                            notifyDataSetChanged(); // 리사이클러 뷰를 갱신
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();


                }
            });

        }
    }


}
