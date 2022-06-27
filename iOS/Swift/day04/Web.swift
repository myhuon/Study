/*
    함수의 매개변수 - 외부에서 쓰는 변수명, 내부에서 쓰는 변수명
    ** 주의 사항
    1) _ 는 생략 가능하다는 의미이다.
    2) 매개변수를 하나로 지정했을 때, 내외부에서 모두 같은 매개변수명을 사용하겠다는 의미이다.
 
    func f1(a b: String){
        b = "c"
    }
    func f2(_ b: Int){
        f1(a : "d")
        b = 1
    }
    func f3( b: String){
        f2(2)
        b = "y"
    }
    func f4(_ c: Int){
        f3(b : "x")
    }
 
 */

/*
    - Web loading을 위한 세팅
    
    'info.plist' 수정
    => Information Property List -> App Transport Security Settings -> Allow Arbitary Loads -> YES
*/

import UIKit
import WebKit

class ViewController: UIViewController, WKNavigationDelegate {

    @IBOutlet var txtUrl: UITextField!
    @IBOutlet var myWebView: WKWebView!
    @IBOutlet var myActiityIndicator: UIActivityIndicatorView!
    
    func loadWebPage(_ url: String){
        let myUrl = URL(string: url)
        let myRequest = URLRequest(url: myUrl!)
        myWebView.load(myRequest)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        myWebView.navigationDelegate = self // delegate를 ViewController로 지정하겠다.
        loadWebPage("http://naver.com")
    }
    
    // activity indicator 구현
    // web page loading
    func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        myActiityIndicator.startAnimating()
        myActiityIndicator.isHidden = false
    }
    
    // loading finish
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        myActiityIndicator.stopAnimating()
        myActiityIndicator.isHidden = true
    }
    
    // load error
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        myActiityIndicator.stopAnimating()
        myActiityIndicator.isHidden = true
    }
    
    func checkUrl(_ url: String) -> String {
        var strUrl = url
        let flag = strUrl.hasPrefix("http://")  // 문자열에 지정한 prefix가 있는지 검사함.
        if !flag{
            strUrl = "http://" + strUrl
        }
        return strUrl
    }
    
    @IBAction func btnGotoUrl(_ sender: UIButton) {
    }
    
    @IBAction func btnGoSite1(_ sender: UIButton) {
        loadWebPage("http://google.com")
    }
    
    @IBAction func btnGoSite2(_ sender: UIButton) {
        loadWebPage("http://dongduk.ac.kr")
    }
    
    @IBAction func btnLoadHtmlString(_ sender: UIButton) {
        let htmlString = "<h1> HTML String </h1><p> String 변수를 이용한 웹 페이지</p><p><a href=\"http://2sam.net\">2sam</a>으로 이동</p>"
        myWebView.loadHTMLString(htmlString, baseURL: nil)
    }
    
    // 파일 경로 읽어서 그 파일 경로를 주소로 한 웹을 로딩한다.
    @IBAction func btnLoadHtmlFile(_ sender: UIButton) {
        let filePath = Bundle.main.path(forResource: "htmlView", ofType: "html")
        let myUrl = URL(fileURLWithPath: filePath!)
        let myRequest = URLRequest(url: myUrl)
        myWebView.load(myRequest)
    }
    
    @IBAction func btnStop(_ sender: UIBarButtonItem) {
        myWebView.stopLoading()
    }
    
    @IBAction func btnReload(_ sender: UIBarButtonItem) {
        myWebView.reload()
    }
    
    @IBAction func btnGoBack(_ sender: UIBarButtonItem) {
        myWebView.goBack()
    }
    
    @IBAction func btnGoForward(_ sender: UIBarButtonItem) {
        myWebView.goForward()
    }
    
}
