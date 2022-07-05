/*
  이미지 크기 자동 설정 : image click -> attributes inspector -> type : Custom, Style : Default
  이미지 크기 동일하게 설정 : image click + control -> 다른 이미지로 드래그 & 드롭 -> Equals Height
*/

import UIKit
import AVKit

class ViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    // 외부의 비디오 재생 플레이어에 파일만 전달하는 형식으로 비디오 재생 사용.
    @IBAction func btnPlayerInternalMovie(_ sender: UIButton) {
        // 내부 파일 mp4 경로 지정
        let filePath:String? = Bundle.main.path(forResource: "FastTyping", ofType: "mp4")
        let url = NSURL(fileURLWithPath: filePath!)
        
        playVideo(url: url)
    }
    
    @IBAction func btnPlayerExternalMovie(_ sender: UIButton) {
        // 외부 파일 mp4
        let url = NSURL(string: "https://dl.dropboxusercontent.com/s/e38auz050w2mvud/Fireworks.mp4")!
        
        playVideo(url: url)
    }
    
    // 접근 제어 지정자
    private func playVideo(url: NSURL) {
        let playerController = AVPlayerViewController()
        
        let player = AVPlayer(url: url as URL)
        playerController.player = player
        
        self.present(playerController, animated: true) {
            player.play()
        }
    }
    
}
