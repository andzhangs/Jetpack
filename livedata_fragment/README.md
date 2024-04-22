# Fragment间的通信

## 1.同级Fragment之间
从FragmentB向FragmentA传递数据，首先给fragmentA设置监听器
``` 
    getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // Bundle支持任意的数据类型
                String result = bundle.getString("bundleKey");
                //做一些其他事情
                doSomething();
            }
        });
```
在FragmentB中，点击发送数据，需要注意的是，FragmentB必须使用和FragmentA一样的FragmentManager，使用相同的requestKey
```
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle result = new Bundle();
            result.putString("bundleKey", "result");
            getParentFragmentManager().setFragmentResult("requestKey", result);
        }
    });
```

## 2.在父Fragment和子Fragment间传递数据
### 子Fragment向父Fragment传递数据
为了从子Fragment向父Fragment传递数据，当调用setFragmentResultListener()，父Fragment应该使用getChildFragmentManager()而非getParentFragmentManager()
```
    getChildFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // Bundle支持任意的数据类型
                String result = bundle.getString("bundleKey");
                //做一些其他事情
                doSomething();
            }
        });
```
### 父Fragment向子Fragment传递数据 同理
在父Fragment中，点击发送数据
```
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle result = new Bundle();
            result.putString("bundleKey", "result");
            getChildFragmentManager().setFragmentResult("requestKey", result);
        }
    });
```

## 3.子Fragment向父Activity传递数据
### 通过在fragment中定义接口与Activity共享数据
```
    //TODO:定义一个接口
    public interface MessageCallback {
      void sendMessage();
    }
    
    private MessageCallback mCallback;

    //TODO:当前fragment从activity重写了回调接口  得到接口的实例化对象
    @Override
    public void onAttach(@NonNull Context context) {
      super.onAttach(context);
      mCallback = (MessageCallback) getActivity();
    }
       
    //TODO:在需要传递数据的地方使用，如：
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          mCallback.sendMessage();
      }
    });
```
### 在父Activity中实现这个接口
```
   public class MessageActivity extends AppCompatActivity implements MessageFragment.MessageCallback {
      @Override
      public void sendMessage() {
        //做一些其他事情
        doSomething();
      }
    
    }
```