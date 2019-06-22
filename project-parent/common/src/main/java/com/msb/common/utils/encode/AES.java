package com.msb.common.utils.encode;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密解密
 */
public class AES {
	    /**
	     * 加密
	     * 
	     * @param content
	     *            待加密内容
	     * @param key
	     *            加密的密钥
	     * @return
	     */
	    public static String encrypt(String content, String key) {
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
				secureRandom.setSeed(key.getBytes());
	            kgen.init(128, secureRandom);
	            SecretKey secretKey = kgen.generateKey();
	            byte[] enCodeFormat = secretKey.getEncoded();
	            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
	            Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				byte[] byteContent = content.getBytes("utf-8");
	            byte[] byteRresult = cipher.doFinal(byteContent);
				return AbBase64.encode(byteRresult);
//				StringBuffer sb = new StringBuffer();
//	            for (int i = 0; i < byteRresult.length; i++) {
//	                String hex = Integer.toHexString(byteRresult[i] & 0xFF);
//	                if (hex.length() == 1) {
//	                    hex = '0' + hex;
//	                }
//	                sb.append(hex.toUpperCase());
//	            }
//	            return sb.toString();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 
	    /**
	     * 解密
	     * 
	     * @param content
	     *            待解密内容
	     * @param key
	     *            解密的密钥
	     * @return
	     */
	    public static String decrypt(String content, String key) {
	        if (content.length() < 1)
	            return null;
	        //byte[] byteRresult = new byte[content.length() / 2];
//	        for (int i = 0; i < content.length() / 2; i++) {
//	            int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
//	            int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
//	            byteRresult[i] = (byte) (high * 16 + low);
//	        }
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");

				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
				secureRandom.setSeed(key.getBytes());

	            kgen.init(128, secureRandom);
	            SecretKey secretKey = kgen.generateKey();
	            byte[] enCodeFormat = secretKey.getEncoded();
	            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
				byte[] byteRresult = AbBase64.decode(content);
	            byte[] result = cipher.doFinal(byteRresult);
	            return new String(result);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    public static void main(String[] args) throws Exception {
	       // String password = PassWordCreate.createPassWord(10);
	        String password = "{\"reportNo\":\"C371500VEH1801234567\",\"reporter\":\"大都\",\"insured\":\"大都\",\"reportTime\":\"2018-06-21 03:59:45\",\"lossTime\":\"2018-06-08 20:57:36\",\"licenseNo\":\"标A8800\",\"vehBrand\":\"宝马\",\n" +
					"\"vin\":\"L8042854525758\",\"vehContactor\":\"小明\",\"mobile\":\"18812345678\",\"dealerCode\":\"284451\",\"dealerName\":\"大都督有限公司\",\"lossArea\":\"本市\",\"accidentPlace\":\"漕宝路\",\"lossDesc\":\"超车\",\n" +
					"\"vehicledetName\":\"宝马汽车越野车adasfdsaf\"}";
	        String aesKey = "9BFA07432C73547D620FD46AF0C846E6";
	        String charSet = "utf-8";
	        //9BFA07432C73547D620FD46AF0C846E6
	        //1C3749E0A5E6321B181F3FE21AB9635A
	        System.out.println("密　钥：" + aesKey);
	        System.out.println("原文：" + password);
	        // 加密
	        String encryptResult = encrypt(password, aesKey);
	        System.out.println("AES加密后：" + encryptResult);
	        // 解密
	        String decryptResult = decrypt(encryptResult, aesKey);
	        System.out.println("AES解密后：" + decryptResult);

	        // base64 加密
//			String base64EncryptResult = AbBase64.encode(password,"utf-8");
//	        System.out.println("base64加密后"+base64EncryptResult);
//
//	        // base64 解密
////			String baseencryptResult = AbBase64.decode(base64EncryptResult,"utf-8");
////			System.out.println("base64解密后"+baseencryptResult);
//			Map<String, Object> keypair =  RSAUtils.genKeyPair();
//			byte[] encryptRSA = RSAUtils.encryptByPublicKey(base64EncryptResult.getBytes(charSet),RSAUtils.getPublicKey(keypair));
//			System.out.println("rsa公钥加密后----"
//					+new String(encryptRSA,charSet));
//			String decryptRSAStr = new String(RSAUtils.decryptByPrivateKey(encryptRSA,RSAUtils.getPrivateKey(keypair)),charSet);
//			System.out.println("rsa私钥解密后----"+decryptRSAStr);
//			System.out.println(base64EncryptResult.equalsIgnoreCase(decryptRSAStr));
	    }
	 
}
