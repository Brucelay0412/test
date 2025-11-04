package com.example.simplecomputer; // 宣告此 Java 類別所屬的 package（與專案結構對應）

// 匯入 Android 與 UI 所需的類別
import androidx.appcompat.app.AppCompatActivity; // 支援 action bar 的 Activity 基底類別
import android.os.Bundle;                     // 用於 Activity 的生命週期資料（例如 onCreate 的參數）
import android.view.View;                     // View 與 OnClickListener 需要
import android.widget.Button;                 // Button 類別
import android.widget.TextView;               // TextView 類別

// MainActivity：應用程式的主要 Activity（畫面）
public class MainActivity extends AppCompatActivity {

    TextView tv;          // TextView 的變數，顯示數字與運算結果
    String current = "";  // current：當前正在輸入的數字（以字串累加，例如 "12.3"）
    String previous = ""; // previous：按下運算符號時儲存的前一個數字（仍為字串）
    String op = "";       // op：目前選擇的運算符號，例如 "+"、"-"、"×"、"÷"

    // Activity 被建立時會呼叫 onCreate → 主要在這裡綁定 layout 與初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           // 呼叫父類別實作
        setContentView(R.layout.activity_main);      // 把 activity_main.xml 佈局載入到這個 Activity

        tv = findViewById(R.id.textView);            // 把 layout 裡 id 為 textView 的元件綁到 tv 變數

        // 這個 numberListener 處理數字按鍵與小數點按鍵的點擊事件
        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;                            // 將被點擊的 View 轉型為 Button
                current += b.getText().toString();               // 把按鈕上的文字（例如 "1"、"."）附加到 current 字串
                tv.setText(current);                              // 將 current 顯示在上方的 TextView（即即時顯示輸入）
            }
        };

        // -------------------- 數字 & 小數點 按鈕綁定 --------------------
        // 把對應的按鈕 id 綁定到上面的 numberListener（輸入 7,8,9,...,0,.）
        findViewById(R.id.button).setOnClickListener(numberListener);   // button = "7"
        findViewById(R.id.button2).setOnClickListener(numberListener);  // button2 = "8"
        findViewById(R.id.button3).setOnClickListener(numberListener);  // button3 = "9"
        findViewById(R.id.button5).setOnClickListener(numberListener);  // button5 = "4"
        findViewById(R.id.button6).setOnClickListener(numberListener);  // button6 = "5"
        findViewById(R.id.button7).setOnClickListener(numberListener);  // button7 = "6"
        findViewById(R.id.button9).setOnClickListener(numberListener);  // button9 = "1"
        findViewById(R.id.button10).setOnClickListener(numberListener); // button10 = "2"
        findViewById(R.id.button11).setOnClickListener(numberListener); // button11 = "3"
        findViewById(R.id.button14).setOnClickListener(numberListener); // button14 = "0"
        findViewById(R.id.button13).setOnClickListener(numberListener); // button13 = "."

        // -------------------- 運算符 按鈕綁定 --------------------
        // 每個運算符使用 opListener(factory) 產生獨立的 listener，傳入對應符號
        findViewById(R.id.button4).setOnClickListener(opListener("÷")); // button4 = "÷"
        findViewById(R.id.button8).setOnClickListener(opListener("×")); // button8 = "×"
        findViewById(R.id.button12).setOnClickListener(opListener("-")); // button12 = "－"
        findViewById(R.id.button16).setOnClickListener(opListener("+")); // button16 = "+"

        // -------------------- 等號 "=" 的邏輯 --------------------
        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 保護式檢查：如果 previous 或 current 為空字串就不做任何運算（避免 parseDouble 空字串崩潰）
                if(previous.equals("") || current.equals("")){
                    return;  // 直接回傳，不進行運算
                }

                // 將 previous 與 current 由字串轉成 double（注意：已做非空檢查）
                double a = Double.parseDouble(previous);
                double b = Double.parseDouble(current);
                double r = 0; // r 用來存放計算結果

                // 根據 op 執行對應的運算
                switch (op) {
                    case "+": r = a + b; break;         // 加法
                    case "-": r = a - b; break;         // 減法
                    case "×": r = a * b; break;         // 乘法
                    case "÷":
                        if (b != 0) r = a / b;         // 除法（除以 0 要避免）
                        else r = 0;                    // 如果 b==0，暫定結果為 0（你也可以顯示錯誤訊息）
                        break;
                }

                // 顯示結果：格式化為小數點後三位（如果你要自動去除多餘零，需額外處理）
                tv.setText(String.format("%.3f", r));

                // 重置 current，並把 previous 設為結果（方便接著做連續計算）
                current = "";
                previous = Double.toString(r);
            }
        });

        // -------------------- C（清除）按鈕 --------------------
        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = "";         // 清空當前輸入
                previous = "";        // 清空前一個數字
                op = "";              // 清空運算符
                tv.setText("0");      // 顯示回預設的 "0"
            }
        });

    } // end onCreate

    // opListener 是一個工廠方法（傳入運算符後回傳一個 OnClickListener）
    // 這樣可以避免為每個運算符重複寫相同的 listener 程式碼
    View.OnClickListener opListener(String oper){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果 current 不為空，代表使用者剛剛輸入了一個數字
                if(!current.equals("")){
                    previous = current; // 把現在輸入的數字存到 previous
                    current = "";       // 清掉 current，準備輸入下一個數字
                }
                // 記錄目前選的運算符號
                op = oper;

                // 顯示：把 previous 與 operator 顯示在 TextView 上（例如 "12 +"）
                // 若 previous 是空字串（例如直接按 +），會只顯示 " " + op → 顯示不友善，建議先輸入數字或改進
                tv.setText(previous + " " + op);
            }
        };
    }

} // end class
