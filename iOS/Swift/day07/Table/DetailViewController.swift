import UIKit

protocol EditDelegate {
    func didMessageEditDone(_ controller: DetailViewController, message: String)
}

class DetailViewController: UIViewController {

    @IBOutlet var lblItem: UILabel!
    @IBOutlet var txItem: UITextField!
    
    var receiveItem = ""
    var itemIndex = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        lblItem.text = receiveItem
    }
    
    func receiveItem(_ item: String){
        receiveItem = item
    }
    
    @IBAction func updateItemText(_ sender: UIButton) {
        
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
