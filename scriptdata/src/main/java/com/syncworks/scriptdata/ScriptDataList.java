package com.syncworks.scriptdata;

import android.support.annotation.NonNull;
import android.util.Log;

import com.syncworks.define.Define;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created with Android Studio
 * Copyrights (C)SyncWorks All rights reserved by SyncWorks
 * Created by 승현 on 2015-03-21.
 * 스크립트 데이터 리스트
 */
public class ScriptDataList implements List<ScriptData> {
	private final static String TAG = ScriptDataList.class.getSimpleName();
    // 변환 데이터
    private float effectRatio = 1;
    private float brightRatio = 1;

	// LED 번호
	private int ledNumber = 0;
	// 스크립트 데이터
	private List<ScriptData> dataList;
	// 현재 실행 순서
	private int currentIndex = 0;
	// 현재 밝기
	private int currentBright = 0;
	// 현재 지연
	private int currentDuration = 0;

	// 시작 인덱스
	private int indexOfStart = 0;
	// FOR 시작 인덱스
	private int indexOfFor = 0;
	// 패스 데이터
	private static int[] passData = new int[9];

	private int forCount = 0;
	private int transitionCount = 0;

    public ScriptDataList() {
        ledNumber = 0;
        dataList = new ArrayList<>();
        initCurrentVar();
    }

	// 기본 생성자
	public ScriptDataList(int ledNum) {
		ledNumber = ledNum;
		dataList = new ArrayList<>();
		initCurrentVar();
	}
	// 생성자 - 데이터리스트 설정
	public ScriptDataList(int ledNum, List<ScriptData> dataList) {
		ledNumber = ledNum;
		this.dataList = dataList;
		initCurrentVar();
	}

	// 변수 초기화
	public void initCurrentVar() {
		currentBright = 0;
		currentDuration = 0;
		currentIndex = 0;
	}

    public void setLedNumber(int ledNum) {
        ledNumber = ledNum;
    }

	// 데이터 추가-인덱스
	@Override
	public void add(int location, ScriptData object) {
		if (getDataListSize() < Define.DATA_ARRAY_MAX + 1) {
			this.dataList.add(location,object);
		}
	}
	// 데이터 추가
	@Override
	public boolean add(ScriptData object) {
        int scriptDataSize = getDataListSize();
        if (scriptDataSize == 0) {
            dataList.add(object);
            return true;
        }
		else if (scriptDataSize < Define.DATA_ARRAY_MAX + 1) {
			int size = dataList.size();
			int val = dataList.get(size-1).getVal();
			if (val == Define.OP_END) {
				dataList.add(size-1,object);
			} else {
				dataList.add(object);
			}
			return true;
		}
		return false;
	}
	// 사용 안함
	@Override
	public boolean addAll(int location, Collection<? extends ScriptData> collection) {
		return dataList.addAll(location, collection);
	}
	// 사용 안함
	@Override
	public boolean addAll(Collection<? extends ScriptData> collection) {
		return dataList.addAll(collection);
	}
	// 데이터 초기화
	@Override
	public void clear() {
		dataList.clear();
		initCurrentVar();
	}
	// 데이터 명령어 확인(int)
	@Override
	public boolean contains(Object object) {
		int val = (int) object;
		int compareVal;
		if (val > Define.OP_CODE_MIN) {
			for (int i = 0; i < dataList.size() - 1; i++) {
				compareVal = dataList.get(i).getVal();
				if (compareVal == val) {
					return true;
				}
			}
		}
		return false;
	}
	// 사용 안함
	@Override
	public boolean containsAll(@NonNull Collection<?> collection) {
		return dataList.containsAll(collection);
	}
	// 데이터 추출
	@Override
	public ScriptData get(int location) {
		if (location < 0) {
			return new ScriptData(0,0);
		} else if (location >= dataList.size()) {
			return new ScriptData(0,0);
		}
		return dataList.get(location);
	}
	// 해당 명령어 확인
	@Override
	public int indexOf(Object object) {
		int val = (int) object;
		int compareVal;
		if (val>Define.OP_CODE_MIN) {
			for (int i=0;i < dataList.size() -1;i++) {
				compareVal = dataList.get(i).getVal();
				if (compareVal == val) {
					return i;
				}
			}
		}
		return -1;
	}
	// 사용 안함
	@Override
	public boolean isEmpty() {
		return dataList.isEmpty();
	}
	// 사용 안함
	@NonNull
	@Override
	public Iterator<ScriptData> iterator() {
		return dataList.iterator();
	}
	// 사용 안함
	@Override
	public int lastIndexOf(Object object) {
		return dataList.lastIndexOf(object);
	}
	// 사용 안함
	@NonNull
	@Override
	public ListIterator<ScriptData> listIterator() {
		return dataList.listIterator();
	}
	// 사용 안함
	@NonNull
	@Override
	public ListIterator<ScriptData> listIterator(int location) {
		return dataList.listIterator(location);
	}
	// 데이터 제거
	@Override
	public ScriptData remove(int location) {
		return dataList.remove(location);
	}
	// 데이터 제거
	@Override
	public boolean remove(Object object) {
		return dataList.remove(object);
	}
	// 사용 안함
	@Override
	public boolean removeAll(@NonNull Collection<?> collection) {
		return dataList.removeAll(collection);
	}
	// 사용 안함
	@Override
	public boolean retainAll(@NonNull Collection<?> collection) {
		return dataList.retainAll(collection);
	}
	// 데이터 수정
	@Override
	public ScriptData set(int location, ScriptData object) {
		return dataList.set(location,object);
	}
	// 데이터 길이
	@Override
	public int size() {
		return dataList.size();
	}
	// 사용 안함
	@NonNull
	@Override
	public List<ScriptData> subList(int start, int end) {
		return dataList.subList(start,end);
	}
	// 사용 안함
	@NonNull
	@Override
	public Object[] toArray() {
		return dataList.toArray();
	}

	public byte[] toByteArray() {
		byte[] byteArray;
		int dataListSize = getDataListSize()*2;
		int byteArrayCount = 0;
		ScriptData thisData;
		byteArray = new byte[dataListSize];
		for (int i=0;i<dataList.size();i++) {
			thisData = dataList.get(i);
			byteArray[byteArrayCount++] = (byte) thisData.getVal();
			byteArray[byteArrayCount++] = (byte) thisData.getDuration();
			if (thisData.getSize() == Define.DOUBLE_SCRIPT) {
				byteArray[byteArrayCount++] = (byte) thisData.getData1();
				byteArray[byteArrayCount++] = (byte) thisData.getData2();
			}
		}
		return byteArray;
	}
	// 사용 안함
	@NonNull
	@Override
	public <T> T[] toArray(T[] array) {
		return dataList.toArray(array);
	}
	// 현재 밝기 확인
	public int getCurrentBright() {
		return currentBright;
	}

	// 데이터 카운트 확인
	public int getDataListSize() {
		int size = 0;
        for (ScriptData sd : dataList) {
            Log.d(TAG, "Data:" + sd.getVal() + "," + sd.getDuration());
            if (sd.getVal() == Define.OP_TRANSITION) {
                size++;
            }
            size++;
        }
		Log.d(TAG,"Size: " + size);
		return size;
	}

	public void DataExecute() {

		// 지속 시간이 끝이 났으면 실행
		if (currentDuration == 0) {
			ScriptData thisData = dataList.get(currentIndex);
			int val = thisData.getVal();
			int duration = thisData.getDuration();
			int data1 = thisData.getData1();
			int data2 = thisData.getData2();
			// 현재 데이터가 명령어가 아니라면
			if (val < Define.OP_CODE_MIN) {
				// 현재 밝기 조정
                setRatioBright(val);
				// 지속 시간 갱신
                setEffectDuration(duration);
//				setDuration(duration);
				// 인덱스 증가
				indexIncrease();
			}
			// 현재 데이터가 명령어라면
			else {
				switch(val) {
					case Define.OP_START:
						// 시작 위치 설정
						indexOfStart = currentIndex;
						// 지속 시간 갱신
						setDuration(duration);
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_END:
						// 지속 시간 갱신
						setDuration(duration);
						// 시작 위치로 복귀
						currentIndex = indexOfStart;
						break;
					case Define.OP_FETCH:
						// 아직 안됨
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_RANDOM_VAL:
						// 랜덤 밝기 획득
						int randVal = getRandomVal(duration);
						// 밝기 설정
                        setRatioBright(randVal);
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_RANDOM_DELAY:
						// 랜덤 지연 획득
						int randDelay = getRandomDelay(duration);
						// 지속 시간 갱신
						setDuration(randDelay);
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_NOP:
						// 지속 시간 갱신
                        setEffectDuration(duration);
//						setDuration(duration);
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_LONG_DELAY:
						// 지속 시간 갱신
						setDuration(duration<<8);
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_FOR_START:
						// FOR 시작 위치 설정
						indexOfFor = currentIndex;
						// 변수 초기화
						forCount = 0;
						// 인덱스 증가
						indexIncrease();
						break;
					case Define.OP_FOR_END:
						int forEnd = (duration>>4) & 0x000F;
						// 비교값이 일치하면
						if (forEnd <= forCount) {
							// 다음 단계로 넘어감
							indexIncrease();
						}
						// 비교값이 일치하지 않으면
						else {
							// 비교값 증가
							forCount++;
							// For 시작 위치로 돌아감
							currentIndex = indexOfFor;
						}
						break;
					case Define.OP_JUMPTO:
						currentIndex = duration;
						break;
					case Define.OP_PASS_DATA:
						int passingLedNum = (duration>>3) & 0x001F;
						int passingData = duration & 0x0007;
						// 데이터 전달
						passData[passingLedNum] = passingData;
						// 인덱스 증가
						indexIncrease();
						break;
					// **** 4바이트 명령어 ****
					// 명령어(1Byte) + 기준값(4bit) + 카운트(4bit) + 초기 밝기(1Byte) +
					// 방향(1bit) + 단계별 밝기(4bit) + 단계별 지연시간(3bit)
					// 밝기 값을 점차 증가 혹은 감소
					case Define.OP_TRANSITION:
						int transitionEnd = (duration>>4) & 0x000F;
                        int plus_minus = 1 + (((data2>>7) & 0x0001) * (-2));
						// 단계별 밝기 설정
						int gapBright = ((data2>>3) & 0x000F) + 1;
						// 단계별 지연 설정
						int gapDuration = 1 << (data2 & 0x0007);
						// 밝기 설정
                        setRatioBright(data1 + transitionCount * (plus_minus * gapBright));
						// 지연 설정
						setDuration(gapDuration);

						// 비교 값이 일치하면
						if (transitionEnd <= transitionCount) {
							// 비교 값 초기화
							transitionCount = 0;
							// 다음 단계로 넘어감
							indexIncrease();
						}
						// 비교 값이 일치하지 않으면
						else {
							// 비교 값 카운트
							transitionCount++;
						}
						break;
				}
			}
		}
		// 전달 인자로 넘어온 값이 인덱스를 넘기라는 명령이면 다음 인덱스로 넘어감
		else if (passData[ledNumber] == Define.PASS_DATA_INCREASE_INDEX) {
			// 현재 지속시간 초기화
			currentDuration = 0;
			// 다음 단계로 넘어감
			indexIncrease();
			// 전달 인자 초기화
			passData[ledNumber] = Define.PASS_DATA_NONE;
		}
		// 무한 지속 시간으로 설정되지 않았다면 현재 지속 시간을 감소
		else if (currentDuration != Define.DELAY_INFINITE) {
			// 현재 지속 시간 감소
			currentDuration--;
		}
		// 무한 지속 유지시간으로 설정되었다면 아무 것도 안함
	}
	// 밝기 설정
	/*private void setBright(int brightness) {
		if (brightness <0 || brightness>= Define.OP_CODE_MIN) {
			return;
		}
		currentBright = brightness;
	}*/
    // Bright Ratio 로 변환된 밝기
    private void setRatioBright(int brightness) {
        if (brightness <0 || brightness>= Define.OP_CODE_MIN) {
            return;
        }
        currentBright = (int) (brightness * brightRatio);
        if (currentBright >= Define.OP_CODE_MIN) {
            currentBright = Define.OP_CODE_MIN -1;
        }
    }

	private void setDuration(int duration) {
		if (duration == 0xFF) {
			currentDuration = Define.DELAY_INFINITE;
		}
		currentDuration = duration;
	}
    private void setEffectDuration(int duration) {
        if (duration == 0xFF) {
            currentDuration = Define.DELAY_INFINITE;
        }
        currentDuration = (int) (duration * effectRatio);
        if (currentDuration >= 0xFF) {
            currentDuration = 0xFF - 1;
        }
    }


	private void indexIncrease() {
		currentIndex++;
		if (currentIndex >= dataList.size()) {
			currentIndex = 0;
		}
	}

	private int getDuration(int duration) {
		if (duration == 0xFF) {
			return Define.DELAY_INFINITE;
		}
		return duration;
	}

	private int getRandomVal(int data) {
		int refVal = ((data>>3) & 0x001F) * 6;
		int gapVal = 1 << (data & 0x0007);
		int rand = (int) (Math.random() * gapVal * 2);
		int retVal = refVal - gapVal + rand;
		if (retVal < 0) {
			retVal = 0;
		} else if (retVal >= Define.OP_CODE_MIN) {
			retVal = Define.OP_CODE_MIN - 1;
		}
		return retVal;
	}

	private int getRandomDelay(int data) {
		int refVal = ((data>>3) & 0x001F) * 6;
		int gapVal = 1 << (data & 0x0007);
		int rand = (int) (Math.random() * gapVal * 2);
		int retVal = refVal - gapVal + rand;
		if (retVal < 0) {
			retVal = 0;
		} else if (retVal >= 0xFF) {
			retVal = 0xFF - 1;
		}
		return retVal;
	}

    public void setEffectRatio(float ratio) {
        this.effectRatio = ratio;
    }
    public void setBrightRatio(float ratio) {
        this.brightRatio = ratio;
    }
    // 시작 지연 설정
    public void setStartDelay(int delay) {
        int size = dataList.size();
        for (int i=0;i<size;i++) {
            int val = dataList.get(i).getVal();
            if (val == Define.OP_START) {
                dataList.set(i,new ScriptData(Define.OP_START,delay));
            }
        }
    }
    // 종료 지연 설정
    public void setEndDelay(int delay) {
        int size = dataList.size();
        for (int i=0;i<size;i++) {
            int val = dataList.get(i).getVal();
            if (val == Define.OP_END) {
                dataList.set(i,new ScriptData(Define.OP_END,delay));
            }
        }
    }


}
