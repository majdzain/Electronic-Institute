package com.ei.zezoo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalcHFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalcHFragment extends Fragment implements XmlClickable{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView lv;
    private DBHelper dbHelper;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private String calcName="";
    private String []EmptyList={"لا يوجد سجل بعد!"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    public CalcHFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalcHFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalcHFragment newInstance(String param1, String param2) {
        CalcHFragment fragment = new CalcHFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc_h, container, false);
        context = view.getContext();
        lv=(ListView)view.findViewById(R.id.listView);
        dbHelper=new DBHelper(context);
        calcName= mParam1;
        list=dbHelper.showHistory(calcName);
        if(!list.isEmpty())
            adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
        else
            adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,EmptyList);
        lv.setAdapter(adapter);
        return view;
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.deleteHistory) {
            dbHelper.deleteRecords(calcName);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, EmptyList);
            lv.setAdapter(adapter);
        }else if(v.getId() == R.id.exit){
            if(calcName.matches("STANDARD")){
                Fragment fragment = new Calc1Fragment();
                ((CalcFragment) getParentFragment()).setCurrentFragment(fragment,1);
            }else if(calcName.matches("SCIENTIFIC")){
                Fragment fragment = new Calc2Fragment();
                ((CalcFragment) getParentFragment()).setCurrentFragment(fragment,2);
            }
        }
    }
}