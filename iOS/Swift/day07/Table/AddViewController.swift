import UIKit

class AddViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet var tfAddItem: UITextField!
    @IBOutlet var pickerImager: UIPickerView!
    @IBOutlet var imageView: UIImageView!

    let MAX_ARRAY_NUM = 3
    let PICKER_VIEW_COLUMN = 1
    let PICKER_VIEW_HEIGHT:CGFloat = 80
    let imageFiles = ["cart.png", "clock.png", "pencil.png"]
    
    var imageArray = [UIImage?]()
    var pickerRow = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        for i in 0..<MAX_ARRAY_NUM {
            let image = UIImage(named: imageFiles[i])
            imageArray.append(image)
        }
        
        imageView.image = imageArray[0]
    }
    
    @IBAction func btnAddItem(_ sender: UIButton) {
        // TableViewController에 있는 전역변수 사용
        items.append(tfAddItem.text!)
        itemsImageFile.append(imageFiles[pickerRow])
        tfAddItem.text = ""
        _ = navigationController?.popViewController(animated: true)
    }
    
    // Picker의 항목이 나타나는 개수 설정
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return PICKER_VIEW_COLUMN
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        // 배열의 크기 : array.count
        return imageFiles.count
    }
    
    // 글자 대신 pickerView에 이미지 넣기
    func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        let imageView = UIImageView(image:imageArray[row])
        imageView.frame = CGRect(x: 0, y: 0, width: 80, height: 80)
        
        return imageView
    }
    
    // label에 선택한 항목의 fileName 넣고, imageView에 image 넣기
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        pickerRow = row
        imageView.image = imageArray[pickerRow]
    }
    
    // pickerView 항목 크기 설정
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return PICKER_VIEW_HEIGHT
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
