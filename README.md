# PhotoPicker
An android library to pick photo from gallery

# Sample
![image](https://github.com/zhaoyun2013/photopicker/blob/master/demo.gif)
<br/>
# Usage
```
Intent intent = new Intent(MainActivity.this, PhotoPickerActivity.class);
intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
startActivityForResult(intent, PICK_PHOTO);
```

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == PICK_PHOTO){
        if(resultCode == RESULT_OK){
            ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            //do what you want to to.
        }
    }
}
```
