package com.cookandroid.bottom_setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.cookandroid.bottom_setting.MainActivity.List_DB;

public class List extends Fragment {

    // Fragment Refresh
    boolean refresh;

    // Bundle에서 ID정보 받아오기
    String id;
    String find;
    Bundle bundle;
    String title;
    String start;
    String end;
    String detail;

    // ID & List_ID 받아오기
    String[] List_ID;
    String final_list_id;
    int length;
    //List fileList = new List();

    //List_ID로 List Data 받아오기
    selectDatabase_list data_list[];
    String find_list[];
    String Title[];
    String List_Term_Start[];
    String List_Term_End[];
    String List_Time_Start[];
    String List_Time_End[];
    String List_Level[];
    String List_Category[];
    String List_Detail[];
    String List_Degree_Goal[];

    // 삭제시 사용할 WebServer 통신 객체
    DeleteDataBase_List_id delete_list_id;
    DeleteDataBase_List delete_list;

    // List Detail 확인용 List
    String List_Detail_List_ID[];
    String List_Detail_Title[];
    String List_Detail_Term_Start[];
    String List_Detail_Term_End[];
    String List_Detail_Time_Start[];
    String List_Detail_Time_End[];
    String List_Detail_Level[];
    String List_Detail_Category[];
    String List_Detail_Detail[];
    String List_Detail_Degree_Goal[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void Refresh_Fragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        refresh = false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        // List Detail 전용 변수 (Naver)
        int a = 0;

        final ListView listview ;
        final List_CustomChoiceListViewAdapter adapter;
        final Intent intent = new Intent(getActivity(), List_SelectGoal.class);
        // 체크박스
        boolean mClick = false;

        // Adapter 생성
        adapter = new List_CustomChoiceListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        bundle = getArguments();

        String IP = getString(R.string.web_IP);

        // Naver 로그인인 경우
        if (bundle != null) {
            id = bundle.getString("id");

            intent.putExtra("id", id);

            // 이용자의 고유 Naver ID 값을 이용해 list_id 정보 불러오기
            selectDatabase_list_id list_id = new selectDatabase_list_id(IP, null, getContext());
            try {
                find = list_id.execute(id).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // List ID JSON Parsing
            try {
                if (!find.equals("null")) {
                    Translate_JSON_List_ID user_list_id = new Translate_JSON_List_ID(find);
                    List_ID = user_list_id.getList_ID();
                    length = user_list_id.getlength();
                }
                else {
                    List_ID = null;
                    length = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                List_ID = null;
                length = 0;
            }
            // List ID로 배열로 List Data 정보 불러오기
            if (List_ID != null) {
                final_list_id = List_ID[length-1];
                data_list = new selectDatabase_list[length];
                find_list = new String[length];
                Title = new String[length];
                List_Term_Start = new String[length];
                List_Term_End = new String[length];
                List_Time_Start = new String[length];
                List_Time_End = new String[length];
                List_Level = new String[length];
                List_Category = new String[length];
                List_Detail = new String[length];
                List_Degree_Goal = new String[length];

                List_Detail_List_ID = new String[length];
                List_Detail_Title = new String[length];
                List_Detail_Term_Start = new String[length];
                List_Detail_Term_End = new String[length];
                List_Detail_Time_Start = new String[length];
                List_Detail_Time_End = new String[length];
                List_Detail_Level = new String[length];
                List_Detail_Category = new String[length];
                List_Detail_Detail = new String[length];
                List_Detail_Degree_Goal = new String[length];

                Translate_JSON_List user_list_data[] = new Translate_JSON_List[length];
                for (int i = 0; i < length; i++) {
                    data_list[i] = new selectDatabase_list(IP, null, getContext());
                    try {
                        find_list[i] = data_list[i].execute(List_ID[i]).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        user_list_data[i] = new Translate_JSON_List(find_list[i]);
                        Title[i] = user_list_data[i].getTitle();
                        List_Term_Start[i] = user_list_data[i].getList_Term_Start();
                        List_Term_End[i] = user_list_data[i].getList_Term_End();
                        List_Time_Start[i] = user_list_data[i].getList_Time_Start();
                        List_Time_End[i] = user_list_data[i].getList_Time_End();
                        List_Level[i] = user_list_data[i].getList_Level();
                        List_Category[i] = user_list_data[i].getList_Category();
                        List_Detail[i] = user_list_data[i].getList_Detail();
                        List_Degree_Goal[i] = user_list_data[i].getList_Degree_Goal();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Title[i] = "";
                        List_Term_Start[i] = "";
                        List_Term_End[i] = "";
                        List_Time_Start[i] = "";
                        List_Time_End[i] = "";
                        List_Level[i] = "";
                        List_Category[i] = "";
                        List_Detail[i] = "";
                        List_Degree_Goal[i] = "";
                    }
                    float total = 0;

                    String strFormat = "yyyy/MM/dd";
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
                    String strToday = sdf.format(date);
                    {
                        try {
                            Date startDate = sdf.parse(List_Term_Start[i]);
                            Date endDate = sdf.parse(List_Term_End[i]);
                            Date today = sdf.parse(strToday);

                            float diffDay = (startDate.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000) * -1;
                            float nowDay = (today.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000) * -1;
                            total = (nowDay / diffDay) * 100;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    String per = (100 - (int) total) + "%";

                    //Title, 시작기간, 끝난 기간, 디테일 , 퍼센트
                    if (total <= 100 && total >= 0) {
                        adapter.addItem(Title[i], List_Term_Start[i], List_Term_End[i], List_Detail[i], per);
                        List_Detail_List_ID[a] = List_ID[i];
                        List_Detail_Title[a] = Title[i];
                        List_Detail_Term_Start[a] = List_Term_Start[i];
                        List_Detail_Term_End[a] = List_Term_End[i];
                        List_Detail_Time_Start[a] = List_Time_Start[i];
                        List_Detail_Time_End[a] = List_Time_End[i];
                        List_Detail_Level[a] = List_Level[i];
                        List_Detail_Category[a] = List_Category[i];
                        List_Detail_Detail[a] = List_Detail[i];
                        List_Detail_Degree_Goal[a] = List_Degree_Goal[i];
                        a++;
                    }
                }

            }
        }

        ImageButton addButton = (ImageButton) view.findViewById(R.id.add);
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                refresh = true;
                startActivity(intent);//액티비티 띄우기
            }
        });

        // delete button에 대한 이벤트 처리. (미완성)
        // 선택해제 기능뿐.
        final ImageButton deleteButton = (ImageButton)view.findViewById(R.id.delete) ;
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                SQLiteDatabase db = List_DB.getWritableDatabase();
                int count = adapter.getCount() ;

                // Naver Login일 경우 웹서버와 통신하여 삭제 처리
                if (bundle != null) {

                    String IP = getString(R.string.web_IP);

                    for (int i = 0; i < count; i++) {
                        if (checkedItems.get(i)) {
                            delete_list_id = new DeleteDataBase_List_id(IP, null, getContext());
                            delete_list = new DeleteDataBase_List(IP, null, getContext());

                            delete_list_id.execute(List_Detail_List_ID[i]);
                            delete_list.execute(List_Detail_List_ID[i]);

                        }
                    }
                    find = "null";
                }
                // Naver Login 이외의 경우 삭제 처리
                else {
                    //adapter.it
                    for (int i = count - 1; i >= 0; i--) {
                        if (checkedItems.get(i)) {
                            //adapter.getItem(i).getGoal();
                            String SQLdelete = "DELETE FROM List_20_11_22 WHERE TITLE = '" + adapter.getItem(i).getGoal() + "'";
                            Log.d("index", adapter.getItem(i).getGoal());
                            db.execSQL(SQLdelete);

                            //if(listview.isItemChecked(i);

                            //adapter.removeItem(1);
                        }
                    }
                    db.close();
                }
                // 모든 선택 상태 초기화.
                listview.clearChoices() ;
                Refresh_Fragment();
                //adapter.notifyDataSetChanged();
            }
        }) ;

        // selectAll button에 대한 이벤트 처리.
        final ImageButton selectAllButton = (ImageButton)view.findViewById(R.id.selectAll) ;
        selectAllButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count = 0 ;
                count = adapter.getCount() ;
                int AllChecked = 0;

                for (int i=0; i<count; i++) {
                    // 전체선택되었는지 확인
                    if(listview.isItemChecked(i) == false) {
                        AllChecked = 1;
                        break;
                    }
                }

                // 전체선택
                if (AllChecked == 1) {
                    for (int i=0; i<count; i++) {
                        if (listview.isItemChecked(i) == false) {
                            listview.setItemChecked(i, true);
                        }
                    }
                }
                else { // 전체해제
                    for (int i=0; i<count; i++) {
                        listview.setItemChecked(i, false) ;
                    }
                }
            }
        }) ;

        // modify button에 대한 이벤트 처리. (미완성)
        final ImageButton modifyButton = (ImageButton)view.findViewById(R.id.modify) ;
        modifyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 모든 선택 상태 초기화.
                listview.clearChoices() ;

                if(selectAllButton.getVisibility() == View.GONE) {
                    selectAllButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    adapter.toggleCheckBox(true);
                } else {
                    selectAllButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    adapter.toggleCheckBox(false);
                }
            }
        }) ;

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (selectAllButton.getVisibility() == View.GONE) {

                    // Naver Login일 경우
                    if (bundle != null) {
                        Intent intent = new Intent(getActivity(), List_Detail.class);
                        intent.putExtra("id", List_Detail_List_ID[position]);
                        intent.putExtra("Title", List_Detail_Title[position]);
                        intent.putExtra("Term_Start", List_Detail_Term_Start[position]);
                        intent.putExtra("Term_End", List_Detail_Term_End[position]);
                        intent.putExtra("Time_Start", List_Detail_Time_Start[position]);
                        intent.putExtra("Time_End", List_Detail_Time_End[position]);
                        intent.putExtra("Level", List_Detail_Level[position]);
                        intent.putExtra("Category", List_Detail_Category[position]);
                        intent.putExtra("Detail", List_Detail_Detail[position]);
                        intent.putExtra("Degree_Goal", List_Detail_Degree_Goal[position]);
                        startActivity(intent);
                    }
                    else {
                        // get item
                        List_ListviewItem item = (List_ListviewItem) parent.getItemAtPosition(position);
                        String Title = item.getGoal();

                        Intent intent = new Intent(getActivity(), List_Detail.class);
                        intent.putExtra("Title", Title);
                        startActivity(intent);
                    }

                    // TODO : use item data.
                }
            }
        }) ;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refresh) {
            Refresh_Fragment();
        }
    }
}