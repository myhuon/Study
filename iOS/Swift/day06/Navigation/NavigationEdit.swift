import UIKit

// java로 말하면 interface와 같음.
protocol EditDelegate {
    func didMessageEditDone(_ controller: EditViewController, message: String)
}

class EditViewController: UIViewController {

    var textWayValue: String = ""
    var textMessage: String = ""
    var delegate: EditDelegate?
    
    @IBOutlet var lblWay: UILabel!
    @IBOutlet var txMessage: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        lblWay.text = textWayValue
        txMessage.text = textMessage
    }
    
    @IBAction func btnDone(_ sender: UIButton) {
        // navigationController : Navigation 생성 시 자동으로 생성되는 변수
        // _ : 리턴 값이 있지만 쓰지 않을 경우 사용
        // popViewController() : ios는 Stack으로 화면 데이터를 저장해 놓고 pop 해서 마지막 화면을 꺼내온다.
        if delegate != nil {
            delegate?.didMessageEditDone(self, message: txMessage.text!)
        }
        
        _ = navigationController?.popViewController(animated: true)
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
