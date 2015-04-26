package com.syncworks.scriptdata;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.syncworks.define.Define;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vosami on 2015-04-22.
 */
public class ScriptFileManager {
    private Context context = null;

    private List<ScriptNameData> singleNameData = null;
    private List<ScriptNameData> colorNameData = null;


    // 생성자 - context 를 입력 받음
    public ScriptFileManager(Context _context) {
        this.context = _context;
        // 변수 초기화
        initVar();
    }

    /**
     * 변수를 초기화
     */
    private void initVar() {
        singleNameData = new ArrayList<>();
        colorNameData = new ArrayList<>();
    }

	public String getFileName(boolean type, int pos) {
		if (type) {
			if (pos >= singleNameData.size()) {
				return Define.FILE_DEFAULT;
			} else {
				return singleNameData.get(pos).getNameOfFile();
			}
		} else {
			if (pos >= colorNameData.size()) {
				return Define.FILE_COLOR_DEFAULT;
			} else {
				return colorNameData.get(pos).getNameOfFile();
			}
		}
	}

	public boolean getDirType(boolean type, int pos) {
		if (type) {
			if (pos >= singleNameData.size()) {
				return Define.DIR_ASSET;
			} else {
				return singleNameData.get(pos).getDirType();
			}
		} else {
			if (pos >= colorNameData.size()) {
				return Define.DIR_ASSET;
			} else {
				return colorNameData.get(pos).getDirType();
			}
		}
	}

    public String[] getArrayName(boolean type,String langType) {
        String[] retStrArray = null;
        int sizeOfArray = 0;
        if (type) {
            sizeOfArray = singleNameData.size();
            retStrArray = new String[sizeOfArray];
            for (int i=0;i<sizeOfArray;i++) {
                retStrArray[i] = getName(singleNameData.get(i),langType);
            }
        } else {
            sizeOfArray = colorNameData.size();
            retStrArray = new String[sizeOfArray];
            for (int i=0;i<sizeOfArray;i++) {
                retStrArray[i] = getName(colorNameData.get(i),langType);
            }
        }
        return retStrArray;
    }

    private String getName(ScriptNameData snData,String langType) {
        String name = null;
        String nameEn = null;
        name = snData.getName();
        nameEn = snData.getNameEn();

        if (langType.equalsIgnoreCase("kr") && name != null) {
            return name;
        } else if (nameEn != null) {
            return nameEn;
        } else {
            return "none";
        }
    }

    public void resetData() {
        singleNameData.clear();
        colorNameData.clear();
        openAssetFile();
        openFilesDir();
    }

    // Asset 폴더의 xml 파일 검색
    public void openAssetFile() {
        ScriptNameData snData = null;
        AssetManager assetManager = context.getAssets();
        String[] fileList;
        try {
            InputStream is = null;
            fileList = assetManager.list("");
            for (int i=0;i<fileList.length;i++) {
                if (fileList[i].contains(".xml")) {
                    is = assetManager.open(fileList[i]);
                    snData = ScriptXmlParser.parseName(is);
                    if (snData.getName() != null || snData.getNameEn() != null) {
                        snData.setDirOfFile(ScriptNameData.DIR_ASSET);
                        snData.setNameOfFile(fileList[i]);
                        if (snData.getType()) {
                            singleNameData.add(snData);
                        } else {
                            colorNameData.add(snData);
                        }
                    }
                    is.close();
                    Log.d("test"+i,snData.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // App 데이터 폴더의 xml 파일 검색
    public void openFilesDir() {
        ScriptNameData snData = null;
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".xml");
            }
        };
        File file = context.getFilesDir();
        File[] files = file.listFiles(filenameFilter);

        for (int i=0;i<files.length;i++) {
            files[i].getName();
            snData = ScriptXmlParser.testParse(files[i]);
            if (snData.getName() != null || snData.getNameEn() != null) {
                snData.setDirOfFile(ScriptNameData.DIR_FILES);
                snData.setNameOfFile(files[i].getName());
                if (snData.getType()) {
                    singleNameData.add(snData);
                } else {
                    colorNameData.add(snData);
                }
            }
        }
    }


}
