package com.ei.zezoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calc2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calc2Fragment extends Fragment implements XmlClickable{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private EditText e1,e2;
    private int count=0;
    private String expression="";
    private String text="";
    private Double result=0.0;
    private DBHelper dbHelper;
    private TextView mode,toggle,square,xpowy,log,sin,cos,tan,sqrt,fact;
    private int toggleMode=1;
    private int angleMode=1;


    public Calc2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calc2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Calc2Fragment newInstance(String param1, String param2) {
        Calc2Fragment fragment = new Calc2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_calc2, container, false);
        context = view.getContext();
        e1 = (EditText) view.findViewById(R.id.editText);
        e2 = (EditText) view.findViewById(R.id.editText2);
        mode = (TextView) view.findViewById(R.id.modet);
        toggle = (TextView) view.findViewById(R.id.togglet);
        square = (TextView) view.findViewById(R.id.squaret);
        xpowy = (TextView) view.findViewById(R.id.xpowyt);
        log = (TextView) view.findViewById(R.id.logt);
        sin = (TextView) view.findViewById(R.id.sint);
        cos = (TextView) view.findViewById(R.id.cost);
        tan = (TextView) view.findViewById(R.id.tant);
        sqrt= (TextView) view.findViewById(R.id.sqrtt);
        fact = (TextView) view.findViewById(R.id.factorialt);

        dbHelper=new DBHelper(context);


        //tags to change the mode from degree to radian and vice versa
        mode.setTag(1);
        //tags to change the names of the buttons performing different operations
        toggle.setTag(1);
        return view;
    }
    @Override
    public void onClick(View v)
    {
        toggleMode=(int)toggle.getTag();
        angleMode=((int)mode.getTag());
        switch (v.getId()) {

            case R.id.toggle:
                //change the button text if switch button is clicked
                if(toggleMode==1)
                {
                    toggle.setTag(2);
                    square.setText(R.string.cube);
                    xpowy.setText(R.string.tenpow);
                    log.setText(R.string.naturalLog);
                    sin.setText(R.string.sininv);
                    cos.setText(R.string.cosinv);
                    tan.setText(R.string.taninv);
                    sqrt.setText(R.string.cuberoot);
                    fact.setText(R.string.Mod);
                }
                else if(toggleMode==2)
                {
                    toggle.setTag(3);
                    square.setText(R.string.square);
                    xpowy.setText(R.string.epown);
                    log.setText(R.string.log);
                    sin.setText(R.string.hyperbolicSine);
                    cos.setText(R.string.hyperbolicCosine);
                    tan.setText(R.string.hyperbolicTan);
                    sqrt.setText(R.string.inverse);
                    fact.setText(R.string.factorial);
                }
                else if(toggleMode==3)
                {
                    toggle.setTag(1);
                    sin.setText(R.string.sin);
                    cos.setText(R.string.cos);
                    tan.setText(R.string.tan);
                    sqrt.setText(R.string.sqrt);
                    xpowy.setText(R.string.xpown);
                }
                break;

            case R.id.mode:
                //change the angle property for trignometric operations if mode button is clicked
                if(angleMode==1)
                {
                    mode.setTag(2);
                    mode.setText(R.string.mode2);
                }
                else
                {
                    mode.setTag(1);
                    mode.setText(R.string.mode1);
                }
                break;

            case R.id.num0:
                e2.setText(e2.getText() + "0");
                break;

            case R.id.num1:
                e2.setText(e2.getText() + "1");
                break;

            case R.id.num2:
                e2.setText(e2.getText() + "2");
                break;

            case R.id.num3:
                e2.setText(e2.getText() + "3");
                break;


            case R.id.num4:
                e2.setText(e2.getText() + "4");
                break;

            case R.id.num5:
                e2.setText(e2.getText() + "5");
                break;

            case R.id.num6:
                e2.setText(e2.getText() + "6");
                break;

            case R.id.num7:
                e2.setText(e2.getText() + "7");
                break;

            case R.id.num8:
                e2.setText(e2.getText() + "8");
                break;

            case R.id.num9:
                e2.setText(e2.getText() + "9");
                break;

            case R.id.pi:
                e2.setText(e2.getText() + "pi");
                break;

            case R.id.dot:
                if (count == 0 && e2.length() != 0) {
                    e2.setText(e2.getText() + ".");
                    count++;
                }
                break;

            case R.id.clear:
                e1.setText("");
                e2.setText("");
                count = 0;
                expression = "";
                break;

            case R.id.backSpace:
                text=e2.getText().toString();
                if(text.length()>0)
                {
                    if(text.endsWith("."))
                    {
                        count=0;
                    }
                    String newText=text.substring(0,text.length()-1);
                    //to delete the data contained in the brackets at once
                    if(text.endsWith(")"))
                    {
                        char []a=text.toCharArray();
                        int pos=a.length-2;
                        int counter=1;
                        //to find the opening bracket position
                        for(int i=a.length-2;i>=0;i--)
                        {
                            if(a[i]==')')
                            {
                                counter++;
                            }
                            else if(a[i]=='(')
                            {
                                counter--;
                            }
                            //if decimal is deleted b/w brackets then count should be zero
                            else if(a[i]=='.')
                            {
                                count=0;
                            }
                            //if opening bracket pair for the last bracket is found
                            if(counter==0)
                            {
                                pos=i;
                                break;
                            }
                        }
                        newText=text.substring(0,pos);
                    }
                    //if e2 edit text contains only - sign or sqrt or any other text functions
                    // at last then clear the edit text e2
                    if(newText.equals("-")||newText.endsWith("sqrt")||newText.endsWith("log")||newText.endsWith("ln")
                            ||newText.endsWith("sin")||newText.endsWith("asin")||newText.endsWith("asind")||newText.endsWith("sinh")
                            ||newText.endsWith("cos")||newText.endsWith("acos")||newText.endsWith("acosd")||newText.endsWith("cosh")
                            ||newText.endsWith("tan")||newText.endsWith("atan")||newText.endsWith("atand")||newText.endsWith("tanh")
                            ||newText.endsWith("cbrt"))
                    {
                        newText="";
                    }
                    //if pow sign is left at the last or divide sign
                    else if(newText.endsWith("^")||newText.endsWith("/"))
                        newText=newText.substring(0,newText.length()-1);
                    else if(newText.endsWith("pi")||newText.endsWith("e^"))
                        newText=newText.substring(0,newText.length()-2);
                    e2.setText(newText);
                }
                break;

            case R.id.plus:
                operationClicked("+");
                break;

            case R.id.minus:
                operationClicked("-");
                break;

            case R.id.divide:
                operationClicked("/");
                break;

            case R.id.multiply:
                operationClicked("*");
                break;

            case R.id.sqrt:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    toggleMode=(int)toggle.getTag();
                    if(toggleMode==1)
                        e2.setText("sqrt(" + text + ")");
                    else if(toggleMode==2)
                        e2.setText("cbrt(" + text + ")");
                    else
                        e2.setText("1/(" + text + ")");
                }
                break;

            case R.id.square:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(toggleMode==2)
                        e2.setText("(" + text + ")^3");
                    else
                        e2.setText("(" + text + ")^2");
                }
                break;

            case R.id.xpowy:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(toggleMode==1)
                        e2.setText("(" + text + ")^");
                    else if(toggleMode==2)
                        e2.setText("10^(" + text + ")");
                    else
                        e2.setText("e^(" + text + ")");
                }
                break;

            case R.id.log:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(toggleMode==2)
                        e2.setText("ln(" + text + ")");
                    else
                        e2.setText("log(" + text + ")");
                }
                break;

            case R.id.factorial:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(toggleMode==2)
                    {
                        e1.setText("(" + text + ")%");
                        e2.setText("");
                    }
                    else
                    {
                        String res="";
                        try
                        {
                            CalculateFactorial cf=new CalculateFactorial();
                            int []arr=cf.factorial((int)Double.parseDouble(String.valueOf(new ExtendedDoubleEvaluator().evaluate(text))));
                            int res_size=cf.getRes();
                            if(res_size>20)
                            {
                                for (int i=res_size-1; i>=res_size-20; i--)
                                {
                                    if(i==res_size-2)
                                        res+=".";
                                    res+=arr[i];
                                }
                                res+="E"+(res_size-1);
                            }
                            else
                            {
                                for (int i=res_size-1; i>=0; i--)
                                {
                                    res+=arr[i];
                                }
                            }
                            e2.setText(res);
                        }
                        catch (Exception e)
                        {
                            if(e.toString().contains("ArrayIndexOutOfBoundsException"))
                            {
                                e2.setText("Result too big!");
                            }
                            else
                                e2.setText("Invalid!!");
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case R.id.sin:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(angleMode==1)
                    {
                        double angle=Math.toRadians(new ExtendedDoubleEvaluator().evaluate(text));
                        if(toggleMode==1)
                            e2.setText("sin(" + angle + ")");
                        else if(toggleMode==2)
                            e2.setText("asind(" + text + ")");
                        else
                            e2.setText("sinh(" + text + ")");
                    }
                    else
                    {
                        if(toggleMode==1)
                            e2.setText("sin(" + text + ")");
                        else if(toggleMode==2)
                            e2.setText("asin(" + text + ")");
                        else
                            e2.setText("sinh(" + text + ")");
                    }
                }
                break;

            case R.id.cos:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(angleMode==1)
                    {
                        double angle=Math.toRadians(new ExtendedDoubleEvaluator().evaluate(text));
                        if(toggleMode==1)
                            e2.setText("cos(" + angle + ")");
                        else if(toggleMode==2)
                            e2.setText("acosd(" + text + ")");
                        else
                            e2.setText("cosh(" + text + ")");
                    }
                    else
                    {
                        if(toggleMode==1)
                            e2.setText("cos(" + text + ")");
                        else if(toggleMode==2)
                            e2.setText("acos(" + text + ")");
                        else
                            e2.setText("cosh(" + text + ")");
                    }
                }
                break;

            case R.id.tan:
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    if(angleMode==1)
                    {
                        double angle=Math.toRadians(new ExtendedDoubleEvaluator().evaluate(text));
                        if(toggleMode==1)
                            e2.setText("tan(" + angle + ")");
                        else if(toggleMode==2)
                            e2.setText("atand(" + text + ")");
                        else
                            e2.setText("tanh(" + text + ")");
                    }
                    else
                    {
                        if(toggleMode==1)
                            e2.setText("tan(" + text + ")");
                        else if(toggleMode==2)
                            e2.setText("atan(" + text + ")");
                        else
                            e2.setText("tanh(" + text + ")");
                    }
                }
                break;

            case R.id.posneg:
                if (e2.length() != 0) {
                    String s = e2.getText().toString();
                    char arr[] = s.toCharArray();
                    if (arr[0] == '-')
                        e2.setText(s.substring(1, s.length()));
                    else
                        e2.setText("-" + s);
                }
                break;

            case R.id.equal:
                /*for more knowledge on DoubleEvaluator and its tutorial go to the below link
                http://javaluator.sourceforge.net/en/home/*/
                if (e2.length() != 0) {
                    text = e2.getText().toString();
                    expression = e1.getText().toString() + text;
                }
                e1.setText("");
                if (expression.length() == 0)
                    expression = "0.0";
                try {
                    //evaluate the expression
                    result = new ExtendedDoubleEvaluator().evaluate(expression);
                    //insert expression and result in sqlite database if expression is valid and not 0.0
                    if (String.valueOf(result).equals("6.123233995736766E-17"))
                    {
                        result=0.0;
                        e2.setText(result + "");
                    }
                    else if(String.valueOf(result).equals("1.633123935319537E16"))
                        e2.setText("infinity");
                    else
                        e2.setText(result + "");
                    if (!expression.equals("0.0"))
                        dbHelper.insert("SCIENTIFIC", expression + " = " + result);
                } catch (Exception e) {
                    e2.setText("Invalid Expression");
                    e1.setText("");
                    expression = "";
                    e.printStackTrace();
                }
                break;

            case R.id.openBracket:
                e1.setText(e1.getText() + "(");
                break;

            case R.id.closeBracket:
                if(e2.length()!=0)
                    e1.setText(e1.getText() +e2.getText().toString()+ ")");
                else
                    e1.setText(e1.getText() + ")");
                break;

            case R.id.history:
                Fragment fragment = CalcHFragment.newInstance("SCIENTIFIC","SCIENTIFIC");
                ((CalcFragment) getParentFragment()).setCurrentFragment(fragment,0);
                break;
        }
    }

    private void operationClicked(String op) {
        if (e2.length() != 0) {
            String text = e2.getText().toString();
            e1.setText(e1.getText() + text + op);
            e2.setText("");
            count = 0;
        } else {
            String text = e1.getText().toString();
            if (text.length() > 0) {
                String newText = text.substring(0, text.length() - 1) + op;
                e1.setText(newText);
            }
        }
    }
}