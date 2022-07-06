/*
https://www.instiz.net/name/32404951
    - 앱에서 Camera 사용하기 위한 info.plist 설정
    1. Privacy - Photo Library Additions Usage Description
    2. Privacy - Photo Library Usage Description
    3. Privacy - Microphone Usage Description
    4. Privacy - Camera Usage Description

    - 아이폰이 안드로이드보다 보안이 좋은 이유
    : 아이폰은 앱들이 샌드박스 형태 안에 있어서 앱들이 허가된 기능들만 쓸 수 있고, 다른 앱 사이에서도 제한이 많다.
*/

// import : 다른 파일이나 클래스 추가
import UIKit    // User Interface와 관련한 클래스를 모아 놓은 파일을 추가한다.
import MobileCoreServices   // iOS에서 사용할 모든 데이터 타입들이 정의되어 있으므로 미디어 타입을 사용하기 위해서 추가한다.

class ViewController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {

    @IBOutlet var imgView: UIImageView!
    
    let imagePicker: UIImagePickerController! = UIImagePickerController()
    var captureImage: UIImage!
    var videoURL: URL!
    var flagImageSave = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func btnCaptureImageFromCamera(_ sender: UIButton) {
        // 외부의 앱을 사용할 시에는 성공,실패의 경우를 생각해야한다. (카메라 앱은 외부 앱)
        if (UIImagePickerController.isSourceTypeAvailable(.camera)) {   // . 붙은 값은 enumeration 값임을 나타내는 것이다.
            flagImageSave = true
            
            imagePicker.delegate = self
            imagePicker.sourceType = .camera
            imagePicker.mediaTypes = ["public.image"]   // 개인 저장소가 아닌 공동의 저장소의 이미지 타입으로 지정
            imagePicker.allowsEditing = false   // 사진 편집기능 가능하도록 할 것인지 설정
            
            present(imagePicker, animated: true, completion: nil)
        } else {
            myAlert("Camera inaccessable", message: "Application cannot access the camera.")
        }
    }
    
    @IBAction func btnLoadingImageFromLibrary(_ sender: UIButton) {
        if (UIImagePickerController.isSourceTypeAvailable(.photoLibrary)) {
            flagImageSave = false
            
            imagePicker.delegate = self
            imagePicker.sourceType = .photoLibrary
            imagePicker.mediaTypes = ["public.image"]
            imagePicker.allowsEditing = true
            
            present(imagePicker, animated: true, completion: nil)
        } else {
            myAlert("Photo album inaccessable", message: "Application cannot access the photo album.")
        }
    }
    
    @IBAction func btnRecordVideoFromCamera(_ sender: UIButton) {
        if (UIImagePickerController.isSourceTypeAvailable(.camera)) {
            flagImageSave = true
            
            imagePicker.delegate = self
            imagePicker.sourceType = .camera
            imagePicker.mediaTypes = ["public.movie"]   // 카메라와 mediaTypes만 다름.
            imagePicker.allowsEditing = false
            
            present(imagePicker, animated: true, completion: nil)
        } else {
            myAlert("Camera inaccessable", message: "Application cannot access the camera.")
        }
    }
    
    @IBAction func btnLoadVideoFromLibrary(_ sender: UIButton) {
        if (UIImagePickerController.isSourceTypeAvailable(.photoLibrary)) {
            flagImageSave = false
            
            imagePicker.delegate = self
            imagePicker.sourceType = .photoLibrary
            imagePicker.mediaTypes = ["public.movie"]
            imagePicker.allowsEditing = true
            
            present(imagePicker, animated: true, completion: nil)
        } else {
            myAlert("Photo album inaccessable", message: "Application cannot access the photo album.")
        }
    }
    
    // imagePickerController 응답 처리
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        let mediaType = info[UIImagePickerController.InfoKey.mediaType] as! NSString
        
        if mediaType.isEqual(to: "public.image" as String) {
            captureImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
            
            if flagImageSave {  // 이미지 저장 작업
                UIImageWriteToSavedPhotosAlbum(captureImage, self, nil, nil)
            }
            
            imgView.image = captureImage
        }
        else if mediaType.isEqual(to: "public.movie" as NSString as String) {
            if flagImageSave {  // 비디오 저장 작업
                videoURL = (info[UIImagePickerController.InfoKey.mediaURL] as! URL)
                
                UISaveVideoAtPathToSavedPhotosAlbum(videoURL.relativePath, self, nil, nil)
            }
        }
        
        // 카메라 닫기
        self.dismiss(animated: true, completion: nil)
    }
    
    // imagePickerController가 취소 되었을 때 카메라 닫기
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func myAlert (_ title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
        let action = UIAlertAction(title: "ok", style: UIAlertAction.Style.default, handler: nil)
        alert.addAction(action)
        self.present(alert, animated: true, completion: nil)
    }

}
