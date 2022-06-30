import UIKit

// class 외부에 선언 because, AddViewController에서 사용하기 때문
var items = ["책 구매", "철수와 약속", "스터디 준비하기"]
var itemsImageFile = ["cart.png", "clock.png", "pencil.png"]

class TableViewController: UITableViewController {

    @IBOutlet var tvListView: UITableView!
    var isFirst = true
    
    // 화면이 보여지기 전에 준비할 작업
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // bar button 코드상에서 생성하기
         self.navigationItem.leftBarButtonItem = self.editButtonItem
    }
    
    // 화면이 보여지기 직전에 할 작업
    override func viewWillAppear(_ animated: Bool) {
        /*
         AddViewController에서 데이터를 삽입하고 pop해서 다시 TableView로 되돌아오면 삽입된 데이터가 바로 보여지지 않음.
         왜냐하면 pop 해서 꺼내온 화면에는 그 전 화면이 보존되어있기 때문에 그 화면으로 되돌아가되, viewWillApear로 데이터를 다시 로드해서 화면을 업데이트한다.
         */
        tvListView.reloadData()
    }
    
    // 화면이 보여지고나서 할 작업
    override func viewDidAppear(_ animated: Bool) {
        let test = "test"
        // 변수를 사용해서 프린트하는 두 가지 방법
        print("message : \(test)")
        print("message : " + test)
    }

    // MARK: - Table view data source

    // 테이블 한 칸에 보여줄 항목의 개수
    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    // row 개수 => 데이터의 개수
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return items.count  // 배열 개수 세기 : .count
    }

    // 위에 tableView 함수 반환값으로 설정한 항목 개수만큼 아래의 tableView를 호출한다.
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // 항목 보여주기 (cell identifier로 구분 - withIdentifier)
        let cell = tableView.dequeueReusableCell(withIdentifier: "myCell", for: indexPath)
        
        // indexPath : 항목의 row index
        cell.textLabel?.text = items[(indexPath as NSIndexPath).row]
        cell.imageView?.image = UIImage(named: itemsImageFile[(indexPath as NSIndexPath).row])
        
        return cell
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */
    
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // 원본 데이터 지우는 기능
            items.remove(at: (indexPath as NSIndexPath).row)
            itemsImageFile.remove(at: (indexPath as NSIndexPath).row)
            
            // 화면에서 지우는 기능
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    
    // delete 글자 변경
    override func tableView(_ tableView: UITableView, titleForDeleteConfirmationButtonForRowAt indexPath: IndexPath) -> String? {
        return "삭제"
    }
    
    // Override to support rearranging the table view. 테이블 목록 재배열 하기
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {
        // 1. 자리를 바꾸려는 데이터 임시 보관
        let itemToMove = items[(fromIndexPath as NSIndexPath).row]
        let itemImageToMove = itemsImageFile[(fromIndexPath as NSIndexPath).row]
        
        // 2. 기존에 있던 자리에서 삭제
        items.remove(at: (fromIndexPath as NSIndexPath).row)
        itemsImageFile.remove(at: (fromIndexPath as NSIndexPath).row)
        
        // 3. 바꾸려는 자리에 데이터 삽입
        items.insert(itemToMove, at: (to as NSIndexPath).row)
        itemsImageFile.insert(itemImageToMove, at: (to as NSIndexPath).row)
    }

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    // 화면이 넘어가기 전에 호출되는 함수
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) { // sender는 선택한 항목
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        
        if segue.identifier == "sgDetail" {
            let cell = sender as! UITableViewCell   // 항목을 UITableViewCell로 타입 캐스팅
            let indexPath = self.tvListView.indexPath(for: cell)
            let detailView = segue.destination as! DetailViewController
            let itemIndex = (indexPath! as NSIndexPath).row
            detailView.receiveItem(items[itemIndex])
            detailView.itemIndex = itemIndex
        }
    }

}
