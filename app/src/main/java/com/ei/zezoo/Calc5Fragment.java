package com.ei.zezoo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calc5Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calc5Fragment extends Fragment implements XmlClickable{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText e1,e2;
    private Spinner s1,s2;
    private int count1=0;
    private ConvertingUnits.Weight ca;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    public Calc5Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calc5Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Calc5Fragment newInstance(String param1, String param2) {
        Calc5Fragment fragment = new Calc5Fragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calc5, container, false);
        context = view.getContext();
        e1=(EditText)view.findViewById(R.id.item1);
        e2=(EditText)view.findViewById(R.id.item2);
        s1=(Spinner)view.findViewById(R.id.spinner1);
        s2=(Spinner)view.findViewById(R.id.spinner2);

        ca=new ConvertingUnits.Weight();
        return view;
    }
@Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.num0:
                e1.setText(e1.getText()+"0");
                break;

            case R.id.num1:
                e1.setText(e1.getText()+"1");
                break;

            case R.id.num2:
                e1.setText(e1.getText()+"2");
                break;

            case R.id.num3:
                e1.setText(e1.getText()+"3");
                break;

            case R.id.num4:
                e1.setText(e1.getText()+"4");
                break;

            case R.id.num5:
                e1.setText(e1.getText()+"5");
                break;

            case R.id.num6:
                e1.setText(e1.getText()+"6");
                break;

            case R.id.num7:
                e1.setText(e1.getText()+"7");
                break;

            case R.id.num8:
                e1.setText(e1.getText()+"8");
                break;

            case R.id.num9:
                e1.setText(e1.getText()+"9");
                break;

            case R.id.dot:
                if (count1==0)
                {
                    e1.setText(e1.getText()+".");
                    count1++;
                }
                break;

            case R.id.clear:
                e1.setText("");
                e2.setText("");
                count1=0;
                break;

            case R.id.backSpace:
                if(e1.length()!=0)
                {
                    String text=e1.getText().toString();
                    if(text.endsWith("."))
                        count1=0;
                    String newText=text.substring(0,text.length()-1);
                    e1.setText(newText);
                }
                break;

            case R.id.equal:
                int item1=s1.getSelectedItemPosition();
                int item2=s2.getSelectedItemPosition();
                double value1=Double.parseDouble(e1.getText().toString());
                double result=evaluate(item1,item2,value1);
                e2.setText(result+"");
                break;
        }
    }

    public double evaluate(int item1,int item2,double value)
    {
        double temp=0.0;
        if(item1==item2)
            return value;
        else
        {
            switch (item1)
            {
                case 0:
                    temp=ca.MilliToKilo(value);
                    break;
                case 1:
                    temp=ca.CentiToKilo(value);
                    break;
                case 2:
                    temp=ca.DeciToKilo(value);
                    break;
                case 3:
                    temp=ca.GramToKilo(value);
                    break;
                case 4:
                    temp=value;
                    break;
                case 5:
                    temp=ca.MetricTonnesToKilo(value);
                    break;
                case 6:
                    temp=ca.PoundsToKilo(value);
                    break;
                case 7:
                    temp=ca.OuncesToKilo(value);
                    break;
            }

            switch (item2)
            {
                case 0:
                    temp=ca.KiloToMilli(temp);
                    break;
                case 1:
                    temp=ca.KiloToCenti(temp);
                    break;
                case 2:
                    temp=ca.KiloToDeci(temp);
                    break;
                case 3:
                    temp=ca.KiloToGram(temp);
                    break;
                case 5:
                    temp=ca.KiloToMetricTonnes(temp);
                    break;
                case 6:
                    temp=ca.KiloToPounds(temp);
                    break;
                case 7:
                    temp=ca.KiloToOunces(temp);
                    break;
            }
            return temp;
        }
    }
}