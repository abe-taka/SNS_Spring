package com.example.demo.components;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Image_manage {

	@Autowired
	Realtime_manage realtime_manage;

	// アカウント画像取得
	private static String uploadPath;

	// アカウント画像のパス
	private String acc_imgpath = null;

	// 投稿画像のパス
	private String chat_imgpath = null;

	// コメント
	String str = null;

	/*--------------- フォルダの生成-----------------*/
	public String Create_CommentFolder(String mailaddress) {

		File commentfolder = new File(
				"C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\comment\\"
						+ mailaddress + "\\");
		if (commentfolder.exists()) {
			// 既に存在している場合
			System.out.println("コメントフォルダは既に作られています。");
		} else {
			// ない場合
			commentfolder.mkdir();
			System.out.println("コメントフォルダを作成しました");
		}
		String commentfolderpath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\comment\\"
				+ mailaddress + "\\";
		return commentfolderpath;
	}

	/*--------------- アカウントページ -----------------*/
	// アカウント画像保存メソッド
	public String Save_AccountImage(Model model, String mailaddress, MultipartFile multipartFile) {
		// 現在時刻取得
		// String real_time = null;
		// real_time = realtime.Realtime_process(real_time);

		// ファイル名をid、リアルタイムを基に設定
		File oldFileName = new File(multipartFile.getOriginalFilename());
		File newFileName = new File(mailaddress + ".jpg");
		// 変換
		oldFileName.renameTo(newFileName);

		File exist_file = new File(
				"C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
						+ mailaddress + "\\" + mailaddress + ".jpg");

		if (exist_file.exists()) {
			// 削除
			exist_file.delete();

		}
		// idを基にフォルダ生成
		File newfile = new File(
				"C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
						+ mailaddress);

		if (newfile.mkdir()) {
			System.out.println("ディレクトリの作成に成功しました");
		} else {
			System.out.println("作成に失敗または既に存在しています");
		}

		// 保存先を定義
		try {

			String uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
					+ mailaddress + "\\";
			byte[] bytes = multipartFile.getBytes();

			// 指定ファイルへ読み込みファイルを書き込み
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(uploadPath + newFileName)));
			// バイナリデータを書き込み(画像データ)
			stream.write(bytes);
			stream.close();
			acc_imgpath = uploadPath + newFileName;
			return acc_imgpath;
		} catch (Exception e) {
			System.out.println("アカウント登録できませんでした");
			return acc_imgpath;
		}
	}

	// アカウント画像取得メソッド
	public void Get_AccountImage(Model model, String mailaddress) {
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
				+ mailaddress + "\\" + mailaddress + ".jpg";

		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {
			StringBuffer data = new StringBuffer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			model.addAttribute("base64image", data.toString());
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");

			// デフォルトの画像データを持ってくる
			String default_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\default.jpg";

			// 画像表示処理
			try (FileInputStream fis = new FileInputStream(default_uploadPath);) {
				StringBuffer data = new StringBuffer();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}
				// base64にエンコード、タイムリーフにデータを渡す
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				data.append("data:image/jpg;base64,");
				data.append(base64);
				model.addAttribute("base64image", data.toString());
			} catch (Exception e2) {
				e.printStackTrace();
				System.out.println("デフォルト画像処理失敗");
			}
		}

	}

	// アカウント画像取得メソッド
	public void Get_TopAccountImage(Model model, String mailaddress) {
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
				+ mailaddress + "\\" + mailaddress + ".jpg";

		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {
			StringBuffer data = new StringBuffer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			model.addAttribute("Top_base64image", data.toString());
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");

			// デフォルトの画像データを持ってくる
			String default_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\default.jpg";

			// 画像表示処理
			try (FileInputStream fis = new FileInputStream(default_uploadPath);) {
				StringBuffer data = new StringBuffer();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}
				// base64にエンコード、タイムリーフにデータを渡す
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				data.append("data:image/jpg;base64,");
				data.append(base64);
				model.addAttribute("Top_base64image", data.toString());
			} catch (Exception e2) {
				e.printStackTrace();
				System.out.println("デフォルト画像処理失敗");
			}
		}

	}

	// アカウント画像取得メソッド
	public String Response_Get_AccountImage(String mailaddress) {
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
				+ mailaddress + "\\" + mailaddress + ".jpg";

		StringBuffer data = new StringBuffer();
		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");

			// デフォルトの画像データを持ってくる
			String default_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\default.jpg";

			// 画像表示処理
			try (FileInputStream fis = new FileInputStream(default_uploadPath);) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}
				// base64にエンコード、タイムリーフにデータを渡す
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				data.append("data:image/jpg;base64,");
				data.append(base64);

			} catch (Exception e2) {
				e.printStackTrace();
				System.out.println("デフォルト画像処理失敗");
			}
		}
		String imgdata = null;
		imgdata = data.toString();
		return imgdata;
	}

	// アカウント画像パス取得メソッド
	public String GetPath_AccountImage(String mailaddress) {
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
				+ mailaddress + "\\" + mailaddress + ".jpg";

		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {
			StringBuffer data = new StringBuffer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			acc_imgpath = uploadPath;
			return acc_imgpath;
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");

			// デフォルトの画像データを持ってくる
			String default_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\default.jpg";

			// 画像表示処理
			try (FileInputStream fis = new FileInputStream(default_uploadPath);) {
				StringBuffer data = new StringBuffer();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}
				// base64にエンコード、タイムリーフにデータを渡す
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				data.append("data:image/jpg;base64,");
				data.append(base64);
				acc_imgpath = uploadPath;
				return acc_imgpath;
			} catch (Exception e2) {
				e.printStackTrace();
				acc_imgpath = uploadPath;
				return acc_imgpath;
			}
		}

	}

	// 登録アーティストプロフィール画像保存＋パス取得
	public String Save_RegisterArtistProfileImage(Model model, String mailaddress, MultipartFile multipartFile,
			String count, String folderid) {

		File newFileName = null;
		String a_ruploadPath = null;
		// 投稿画像を保存
		try {

			// フォルダを生成
			File newfile = new File(
					"C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\artist_register\\"
							+ folderid + mailaddress);

			if (newfile.mkdir()) {
				System.out.println("アーティスト登録プロフィール画像保存フォルダのディレクトリの作成に成功しました");
			} else {
				System.out.println("アーティスト登録プロフィール画像保存フォルダのディレクトリの作成に失敗しました");
			}

			// 保存先の指定
			a_ruploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\artist_register\\"
					+ folderid + mailaddress + "\\";

			// 保存処理
			// 画像ファイルを生成
			File oldFileName = new File(multipartFile.getOriginalFilename());
			newFileName = new File(count + mailaddress + ".jpg");
			// 変換
			oldFileName.renameTo(newFileName);

			// 指定ファイルへ読み込みファイルを書き込み
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(a_ruploadPath + newFileName)));

			// バイナリデータを書き込み(画像データ)
			byte[] bytes = multipartFile.getBytes();
			stream.write(bytes);
			stream.close();

		} catch (Exception e) {
			System.out.println("アーティスト登録プロフィール画像保存することができませんでした。");
		}
		chat_imgpath = a_ruploadPath + newFileName;
		return chat_imgpath;
	}

	// 登録アーティストプロフィール画像データ取得
	public String Get_RegisterArtistProfileImage(String dbpath) {
		uploadPath = dbpath;
		StringBuffer data = new StringBuffer();

		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}
			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			System.out.println("登録アーティストプロフィール画像を取得することができました。");
		}
		// まだ画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("登録アーティストプロフィール画像を取得することができませんでした。");
			String icon_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\chat_send\\icon\\maru.jpg";

			try (FileInputStream fis = new FileInputStream(icon_uploadPath);) {
				data = new StringBuffer();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}

				// 画像データをbaseにエンコード
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				// 画像タイプはJPEG
				// Viewへの受け渡し。append("data:~~)としているとtymleafでの表示が楽になる
				data.append("data:image/jpg;base64,");
				data.append(base64);

			} catch (Exception e2) {
				e.printStackTrace();
				System.out.println("〇画像取得失敗");
			}
		}
		return data.toString();
	}

	/*------------------ 投稿 ---------------------*/
	// 〇画像を取得
	public void Get_MaruImage(Model model) {
		String icon_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\chat_send\\icon\\maru.jpg";

		try (FileInputStream fis = new FileInputStream(icon_uploadPath);) {
			StringBuffer data = new StringBuffer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// 画像データをbaseにエンコード
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			// 画像タイプはJPEG
			// Viewへの受け渡し。append("data:~~)としているとtymleafでの表示が楽になる
			data.append("data:image/jpg;base64,");
			data.append(base64);
			model.addAttribute("iconimage", data.toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("〇画像取得失敗");
		}
	}

	// 白画像を取得
	public String Get_ReturnMaruImage() {
		String icon_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\chat_send\\icon\\maru.jpg";
		StringBuffer data = new StringBuffer();
		try (FileInputStream fis = new FileInputStream(icon_uploadPath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// 画像データをbaseにエンコード
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			// 画像タイプはJPEG
			// Viewへの受け渡し。append("data:~~)としているとtymleafでの表示が楽になる
			data.append("data:image/jpg;base64,");
			data.append(base64);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("〇画像取得失敗");
		}
		return data.toString();
	}

	// 白画像を取得
	public String Get_ReturnWhiteImage() {
		String icon_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\white.jpeg";
		StringBuffer data = new StringBuffer();
		try (FileInputStream fis = new FileInputStream(icon_uploadPath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// 画像データをbaseにエンコード
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			// 画像タイプはJPEG
			// Viewへの受け渡し。append("data:~~)としているとtymleafでの表示が楽になる
			data.append("data:image/jpg;base64,");
			data.append(base64);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("〇画像取得失敗");
		}
		return data.toString();
	}

	// 投稿画像を保存
	public String Save_ChatImage(Model model, String mailaddress, MultipartFile multipartFile, String count) {

		File newFileName = null;
		String chatuploadPath = null;
		// 投稿画像を保存
		try {
			// 現在日時情報で初期化されたインスタンスの生成
			Date img_dateObj = new Date();
			SimpleDateFormat img_format = new SimpleDateFormat("yyyyMMddHHmmss");
			// 日時情報を指定フォーマットの文字列で取得
			String img_regist_time = img_format.format(img_dateObj);

			// フォルダを生成
			File newfile = new File(
					"C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\chat_send\\"
							+ mailaddress + img_regist_time);

			if (newfile.mkdir()) {
				System.out.println("投稿画像保存フォルダのディレクトリの作成に成功しました");
			} else {
				System.out.println("投稿画像保存フォルダのディレクトリの作成に失敗しました");
			}

			// 保存先の指定
			chatuploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\chat_send\\"
					+ mailaddress + img_regist_time + "\\";

			// 保存処理
			// 画像ファイルを生成
			File oldFileName = new File(multipartFile.getOriginalFilename());
			newFileName = new File(count + mailaddress + ".jpg");
			// 変換
			oldFileName.renameTo(newFileName);

			// 指定ファイルへ読み込みファイルを書き込み
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(chatuploadPath + newFileName)));

			// バイナリデータを書き込み(画像データ)
			byte[] bytes = multipartFile.getBytes();
			stream.write(bytes);
			stream.close();

		} catch (Exception e) {
			System.out.println("投稿画像保存することができませんでした。");
		}
		chat_imgpath = chatuploadPath + newFileName;
		return chat_imgpath;
	}

	// 投稿、アーティスト画像取得メソッド
	public String Get_ChatSendImage(String dbpath) {
		uploadPath = dbpath;
		StringBuffer data = new StringBuffer();

		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}

				os.write(buffer, 0, len);

			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			System.out.println("投稿画像を取得することができました。");
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("投稿画像を取得することができませんでした。");
		}
		return data.toString();
	}

	/*------------------ コメント ---------------------*/
	// コメント画像保存メソッド
	public String Save_CommentImage(String mailaddress, MultipartFile multipartFile, String commentimg_path,
			String real_time) {

		// ファイル名をid、リアルタイムを基に設定
		File oldFileName = new File(multipartFile.getOriginalFilename());
		File newFileName = new File(mailaddress + real_time + ".jpg");
		// 変換
		oldFileName.renameTo(newFileName);

		// 画像保存処理
		try {
			// multipartFile→byteに変換(画像ファイルを書き込むために行う)
			byte[] bytes = multipartFile.getBytes();

			// 保存先を定義
			// BufferedOutputStream : バイト出力ストリームクラス
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(commentimg_path + newFileName)));
			// バイナリに変換した画像データを指定したディレクトリに書き込む
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			System.out.println("コメント画像登録できませんでした");
		}

		// 戻り値(保存ディレクトリ)

		String cimg_path = commentimg_path + newFileName;
		return cimg_path;
	}

	// コメント投稿画像取得メソッド
	public String GetPath_CommentImage(String db_commentimgpath) {

		StringBuffer data = new StringBuffer();
		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(db_commentimgpath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");
		}

		return data.toString();
	}

	// コメントアカウント画像取得メソッド
	public String Comment_AccountImage(Model model, String mailaddress) {
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
				+ mailaddress + "\\" + mailaddress + ".jpg";
		// 画像表示処理
		StringBuffer data = new StringBuffer();
		try (FileInputStream fis = new FileInputStream(uploadPath);) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			str = data.toString();
			// model.addAttribute("commentimage", data.toString());
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");

			// デフォルトの画像データを持ってくる
			String default_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\default.jpg";

			// 画像表示処理
			try (FileInputStream fis = new FileInputStream(default_uploadPath);) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}
				// base64にエンコード、タイムリーフにデータを渡す
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				data.append("data:image/jpg;base64,");
				data.append(base64);
				str = data.toString();
				// model.addAttribute("commentimage", str);
			} catch (Exception e2) {
				e.printStackTrace();
				System.out.println("デフォルト画像処理失敗");
			}
		}
		return str;
	}

	/*------------------ DM ---------------------*/
	// DM送信画像保存メソッド
	public String DM_SendImage(Model model, String mailaddress, MultipartFile multipartFile, String real_time) {

		// 画像のファイル名を設定
		File oldFileName = new File(multipartFile.getOriginalFilename());
		File newFileName = new File(mailaddress + real_time + ".jpg");
		oldFileName.renameTo(newFileName);

		// idを基にフォルダ生成
		File newfile = new File(
				"C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\dm\\"
						+ mailaddress + "\\");

		if (newfile.mkdir()) {
			System.out.println("ディレクトリの作成に成功しました");
		} else {
			System.out.println("作成に失敗または既に存在しています");
		}

		// 保存先を定義
		String dmuploadPath = null;
		try {
			dmuploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\dm\\"
					+ mailaddress + "\\";
			byte[] bytes = multipartFile.getBytes();

			// 指定ファイルへ読み込みファイルを書き込み
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(dmuploadPath + newFileName)));
			// バイナリデータを書き込み(画像データ)
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			System.out.println("DM画像登録することができませんでした");
		}
		String uploadpath = dmuploadPath + newFileName;
		return uploadpath;
	}

	// DM送信画像取得メソッド
	public String Get_DMImage(String mailaddress, String imgpath, String sendtime) {
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\dm\\"
				+ mailaddress + "\\" + mailaddress + sendtime + ".jpg";

		StringBuffer data = new StringBuffer();
		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			// model.addAttribute("base64image", data.toString());
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("DM画像がありません");
		}
		return data.toString();
	}
}
