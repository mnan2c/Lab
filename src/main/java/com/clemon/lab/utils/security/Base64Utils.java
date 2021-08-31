package com.clemon.lab.utils.security;

import cn.hutool.core.codec.Base64;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Base64 编解码工具类。<br>
 * 0. Base64字符表，包括大写A-Z小写a-z数字0-9和+以及/。<br>
 * 1. base64不是加密算法，只是一种编码方式，数据从一种形式转换为另一种形式进行传输/存储。<br>
 * 2. 主流编程语言中，都内置的base64模块，可以直接调用，无需自己重复造轮子. <br>
 * 3. 应用场景：1）html中的图片用base64表示；2）邮件传输，base64编码使得在电子邮件中传输图片成为可能；<br>
 * 4. 除了基本的base64，还有一种url safe 形式的编码方式，目的是将“+/” 替换成 “-_”，
 * 因为标准的Base64并不适合直接放在URL里传输，URL编码器会把标准Base64中的“/”和“+”字符变为形如“%XX”的形式，而这些“%”号在存入数据库时还需要再进行转换。<br>
 * 5. 编码后的数据~=编码 前数据的4/3，会大1/3左右。<br>
 * 6. 对ABC进行Base64编码过程：<br>
 * - 首先取ABC对应的ASCII码值 A : 65、B : 66、C : 67 <br>
 * - 再取二进制值 A : 01000001、B : 01000010、C : 01000011 <br>
 * - 然后把这三个字节的二进制码接起来 010000010100001001000011 <br>
 * - 再以6位为单位分成4个数据块并在最高位填充两个0后形成4个字节的编码后的值 00010000、00010100、00001001、00000011 <br>
 * - 再把这4个字节数据转化成10进制数 16、20、19、3 <br>
 * - 最后根据Base64给出的64个基本字符表，查出对应的ASCII码字符 Q、U、J、D 这里的值实际就是数据在字符表中的索引。 *
 * 解码过程就是把4个字节再还原成3个字节再根据不同的数据形式把字节数组重新整理成数据。<br>
 * <br>
 * 7. 命令行进行Base64编码和解码：<br>
 * (A进行编码后是QQ==) <br>
 * 编码 $ echo A | base64 <br>
 * 解码 $ echo QQ== |base64 -d
 *
 * @description
 * @author mnzhang
 */
@Slf4j
public class Base64Utils {

  /**
   * base64 编码
   *
   * @param data
   * @return
   */
  public static String encode(byte[] data) {
    return new BASE64Encoder().encodeBuffer(data);
  }

  /**
   * encode url safe
   *
   * @param data
   * @return
   */
  public static String encodeUrlSafe(byte[] data) {
    return Base64.encodeUrlSafe(data);
  }

  /**
   * base64 解码
   *
   * @param data
   * @return
   */
  public static byte[] decode(String data) {
    try {
      return new BASE64Decoder().decodeBuffer(data);
    } catch (IOException e) {
      log.warn("failed to decode data: {}", data);
      return new byte[] {};
    }
  }

  public static void main(String[] args) {
    String str =
        "可以创建一个配置项，其中包含执行的定时任务类名，这样可以在部署服务时手动分散定时任务到不同的服务器上。"
            + "该方法虽然解决了资源分配不均衡的问题，不过依然存在单点风险，同时增加了运维管理难度。uu#$";
    String encodeStr = encode(str.getBytes());

    // 一个中文、中文标点3个字节，英文及符号一个字节。
    System.out.println(str.getBytes().length);
    // 英文及符号一个字节
    System.out.println(encodeStr.getBytes().length);

    byte[] decodeBytes = decode(encodeStr);
    System.out.println(encodeStr);
    System.out.println(new String(decodeBytes));

    String urlSafeStr = encodeUrlSafe(str.getBytes());
    byte[] decodeBytes2 = decode(urlSafeStr.replaceAll("-", "+"));
    System.out.println(urlSafeStr);
    System.out.println(new String(decodeBytes2));
  }
}
// 输出：
// 283
// 390
// 5Y+v5Lul5Yib5bu65LiA5Liq6YWN572u6aG577yM5YW25Lit5YyF5ZCr5omn6KGM55qE5a6a5pe2
// 5Lu75Yqh57G75ZCN77yM6L+Z5qC35Y+v5Lul5Zyo6YOo572y5pyN5Yqh5pe25omL5Yqo5YiG5pWj
// 5a6a5pe25Lu75Yqh5Yiw5LiN5ZCM55qE5pyN5Yqh5Zmo5LiK44CC6K+l5pa55rOV6Jm954S26Kej
// 5Yaz5LqG6LWE5rqQ5YiG6YWN5LiN5Z2H6KGh55qE6Zeu6aKY77yM5LiN6L+H5L6d54S25a2Y5Zyo
// 5Y2V54K56aOO6Zmp77yM5ZCM5pe25aKe5Yqg5LqG6L+Q57u0566h55CG6Zq+5bqm44CCdXUjJA==
//
// 可以创建一个配置项，其中包含执行的定时任务类名，这样可以在部署服务时手动分散定时任务到不同的服务器上。该方法虽然解决了资源分配不均衡的问题，不过依然存在单点风险，同时增加了运维管理难度。uu#$
// 5Y-v5Lul5Yib5bu65LiA5Liq6YWN572u6aG577yM5YW25Lit5YyF5ZCr5omn6KGM55qE5a6a5pe25Lu75Yqh57G75ZCN77yM6L-Z5qC35Y-v5Lul5Zyo6YOo572y5pyN5Yqh5pe25omL5Yqo5YiG5pWj5a6a5pe25Lu75Yqh5Yiw5LiN5ZCM55qE5pyN5Yqh5Zmo5LiK44CC6K-l5pa55rOV6Jm954S26Kej5Yaz5LqG6LWE5rqQ5YiG6YWN5LiN5Z2H6KGh55qE6Zeu6aKY77yM5LiN6L-H5L6d54S25a2Y5Zyo5Y2V54K56aOO6Zmp77yM5ZCM5pe25aKe5Yqg5LqG6L-Q57u0566h55CG6Zq-5bqm44CCdXUjJA
// 可以创建一个配置项，其中包含执行的定时任务类名，这样可以在部署服务时手动分散定时任务到不同的服务器上。该方法虽然解决了资源分配不均衡的问题，不过依然存在单点风险，同时增加了运维管理难度。uu#$#
