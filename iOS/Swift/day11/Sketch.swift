//
//  ViewController.swift
//  Sketch
//
//  Created by hyunsoo on 2022/07/07.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet var imgView: UIImageView!
    
    var lastPoint: CGPoint!
    var lineSize: CGFloat = 2.0
    var lineColor = UIColor.red.cgColor
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func btnClearImageView(_ sender: UIButton) {
        imgView.image = nil     // imgView에 있는 이미지 지우기 방법 1
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        let touch = touches.first! as UITouch
            
        lastPoint = touch.location(in: imgView)     // tap(click)한 첫 번째 위치 저장하기
    }
        
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        UIGraphicsGetCurrentContext()?.setStrokeColor(lineColor)
        UIGraphicsGetCurrentContext()?.setLineCap(CGLineCap.round)
        UIGraphicsGetCurrentContext()?.setLineWidth(lineSize)
        
        let touch = touches.first! as UITouch   // 현재 터치 가져오기
        let currPoint = touch.location(in: imgView)     // 터치 위치 가져오기
        
        imgView.image?.draw(in: CGRect(x: 0, y: 0, width: imgView.frame.size.width, height: imgView.frame.size.height))     // 현재 이미지뷰에 마지막 이미지를 다시 그리는 것 (기존에 그려진 것에서 추가해서 그리는것이 아니라 전에 그려진 것을 저장해놓고 다시 그리는 것임)
        
        UIGraphicsGetCurrentContext()?.move(to: CGPoint(x: lastPoint.x, y: lastPoint.y))    // 마지막 위치에서 이동
        UIGraphicsGetCurrentContext()?.addLine(to: CGPoint(x: currPoint.x, y: currPoint.y)) // 계속 저장하기
        UIGraphicsGetCurrentContext()?.strokePath()     // 추가한 선을 context에 그리기
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        lastPoint = currPoint   // 현재 위치를 마지막 위치로 지정한다. (다음 위치까지 이어지기 위해서)
    }
        
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        UIGraphicsGetCurrentContext()?.setStrokeColor(lineColor)
        UIGraphicsGetCurrentContext()?.setLineCap(CGLineCap.round)
        UIGraphicsGetCurrentContext()?.setLineWidth(lineSize)
        
        imgView.image?.draw(in: CGRect(x: 0, y: 0, width: imgView.frame.size.width, height: imgView.frame.size.height))
        
        UIGraphicsGetCurrentContext()?.move(to: CGPoint(x: lastPoint.x, y: lastPoint.y))
        UIGraphicsGetCurrentContext()?.addLine(to: CGPoint(x: lastPoint.x, y: lastPoint.y))     // 끝날거니까 마지막 위치를 저장함.
        UIGraphicsGetCurrentContext()?.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    override func motionEnded(_ motion: UIEvent.EventSubtype, with event: UIEvent?) {
        if motion == .motionShake {     // motion의 enumeration으로 저장되어 있는 .motionShake를 사용해서 흔들기 motion을 판별한다.
            imgView.image = nil     // 이미지 지우기 방법 2
        }
    }
}
