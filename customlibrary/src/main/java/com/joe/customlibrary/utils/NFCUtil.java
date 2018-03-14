package com.joe.customlibrary.utils;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;

import com.joe.customlibrary.common.CommonUtils;

import java.util.Arrays;

/**
 * Created by qiaojianfeng on 17/12/15.
 */

public class NFCUtil {

    /**
     * 校验设备是否支持NFC
     * @param nfcAdapter
     * @return
     */
    public static boolean checkIsSupportNfc(NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) {
            return false;
        }
        return true;
    }

    /**
     * 校验设备是否开启NFC
     * @param nfcAdapter
     * @return
     */
    public static boolean checkIsNfcOpen(NfcAdapter nfcAdapter) {
        if (nfcAdapter!= null && nfcAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

    /**
     * 读取RFID/NFC的UID
     * @param intent
     * @return
     */
    public static String readNfcUid(Intent intent) {
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        return CommonUtils.bytesToHexString(detectedTag.getId());
    }

    /**
     * 读取NFC标签文本数据
     */
    public static String readNfcTag(Intent intent) {
        String nfcText = "";
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    nfcText = parseTextRecord(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nfcText;
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

}
