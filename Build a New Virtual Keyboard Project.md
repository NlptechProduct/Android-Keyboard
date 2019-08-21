# Build a New Virtual Keyboard Project 

Guideline on how to create a virtual keyboard or add more features to your applications. 

## 1. Code

You may edit the sample project or use the code from the sample project in your own project. 

## 2. Replace appkey

Replace the ‘nlptech_appkey’ in the project AndroidManifest.xml with your own appkey. If you do not have your own appkey, get it [here](http://zengine.nlptech.com/register). 

AndroidManifest.xml:
    
```
<application
    <meta-data
        android:name="nlptech_appkey"
        android:value="{appkey_value}" />
</application>
```


## 3. Change the Package Name

You may edit the code in sample project, but you will need to change the package name. 
