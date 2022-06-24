/*
    deligate란 다른 클래스에서 구현하도록 위임하는 것이다.
    PickerView를 변형하기 위해서 deligate를 사용한다.
 */

import UIKit

// 클래스 상속
class ViewController: UIViewController,UIPickerViewDelegate, UIPickerViewDataSource {

    let MAX_ARRAY_NUM = 10
    let PICKER_VIEW_COLUMN = 2
    let PICKER_VIEW_HEIGHT:CGFloat = 80
    var imageArray = [UIImage?]()   // 빈 이미지 배열 만들기
    var imageFileName = ["1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg",
                         "6.jpg", "7.jpg", "8.jpg", "9.jpg", "10.jpg"]
    
    @IBOutlet weak var pickerImage: UIPickerView!
    @IBOutlet weak var lblImageFileName: UILabel!
    @IBOutlet weak var imageView: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        for i in 0..<MAX_ARRAY_NUM{
            let image = UIImage(named: imageFileName[i])
            imageArray.append(image)    // 배열에 항목 추가
        }
        
        lblImageFileName.text = imageFileName[0]
        imageView.image = imageArray[0]
    }

    // Picker의 항목이 나타나는 개수 설정
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return PICKER_VIEW_COLUMN
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        // 배열의 크기 : array.count
        return imageFileName.count
    }
    
    // pickerView 항목을 글자로 나타내기. row : 스크롤 시 row index를 반환함
    /*func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String?{
        return imageFileName[row]
    }*/
    
    // 글자 대신 pickerView에 이미지 넣기
    func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        let imageView = UIImageView(image:imageArray[row])
        imageView.frame = CGRect(x: 0, y: 0, width: 100, height: 150)
        
        return imageView
    }
    
    // label에 선택한 항목의 fileName 넣고, imageView에 image 넣기
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        // component는 왼쪽부터 시작함 (0부터)
        if(component == 0){ // 첫번째 pickerView이면 label 변경
            lblImageFileName.text = imageFileName[row]
        }
        else{   // 두번째 pickerView이면 이미지 변경
            imageView.image = imageArray[row]
        }
    }
    
    // pickerView 항목 크기 설정
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return PICKER_VIEW_HEIGHT
    }
}
