//
//  ViewController.swift
//  DrawGraphics
//
//  Created by hyunsoo on 2022/07/06.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet var imgView: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func btnDrawLine(_ sender: UIButton) {
        UIGraphicsBeginImageContext(imgView.frame.size)     // 그림을 그리는 영역 저장하고, imageContext start
        let context = UIGraphicsGetCurrentContext()!    // 현재 그래픽 컨텍스트 가져오기
        
        // Draw Line
        context.setLineWidth(2.0)   // 선 굵기 지정
        context.setStrokeColor(UIColor.red.cgColor)     // 선 색상 지정
        
        context.move(to: CGPoint(x: 70, y : 50))    // 포인트 이동   ((0,0) : 좌측 상단 , 우측 아래로 갈수록 숫자가 커진다.)
        context.addLine(to: CGPoint(x: 270, y: 250))    // 그리기
        
        context.strokePath()    // 설정한 정보를 그릴 수 있도록 준비
        
        // Draw Triangle
        context.setLineWidth(4.0)
        context.setStrokeColor(UIColor.blue.cgColor)
        
        context.move(to: CGPoint(x: 170, y:200))
        context.addLine(to: CGPoint(x: 270, y: 350))
        context.addLine(to: CGPoint(x: 70, y: 350))
        context.addLine(to: CGPoint(x: 170, y: 200))
        context.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()     // 저장된 그리기 정보들을 가지고 이미지를 실제로 그리라는 명령
        UIGraphicsEndImageContext()     // imageContext end
    }
    
    @IBAction func btnDrawRectangle(_ sender: UIButton) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        let context = UIGraphicsGetCurrentContext()
        
        // Draw Rectangle
        context?.setLineWidth(2.0)
        context?.setStrokeColor(UIColor.red.cgColor)
        
        context?.addRect(CGRect(x: 70, y: 100, width: 200, height: 100))
        context?.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    @IBAction func btnDrawCircle(_ sender: UIButton) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        let context = UIGraphicsGetCurrentContext()
        
        // Draw Ellipse
        context?.setLineWidth(2.0)
        context?.setStrokeColor(UIColor.red.cgColor)
        
        context?.addEllipse(in: CGRect(x: 70, y: 50, width: 200, height: 100))   // 사각형에 '내접'하는 원을 그린다.
        context?.strokePath()
        
        // Draw Circle
        context?.setLineWidth(5.0)
        context?.setStrokeColor(UIColor.green.cgColor)
        
        let circle = CGRect(x: 70, y: 200, width: 200, height: 200)
        context?.addEllipse(in: circle)   // 사각형에 '내접'하는 원을 그린다.
        context?.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    @IBAction func btnDrawArc(_ sender: UIButton) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        let context = UIGraphicsGetCurrentContext()
        
        // Draw Arc
        context?.setLineWidth(5.0)
        context?.setStrokeColor(UIColor.red.cgColor)
        
        context?.move(to: CGPoint(x: 100, y: 50))
        context?.addArc(tangent1End: CGPoint(x: 250, y: 50), tangent2End: CGPoint(x: 250, y: 200), radius: CGFloat(50))     // 세 점을 이은 선에 접하는 호를 그린다.
        context?.addLine(to: CGPoint(x: 250, y: 200))
        
        context?.move(to: CGPoint(x: 100, y: 250))
        context?.addArc(tangent1End: CGPoint(x: 270, y: 250), tangent2End: CGPoint(x: 100, y: 400), radius: CGFloat(20))
        context?.addLine(to: CGPoint(x: 100, y: 400))
        
        context?.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    @IBAction func btnDrawFill(_ sender: UIButton) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        let context = UIGraphicsGetCurrentContext()!
        
        // Draw Rectangle
        context.setLineWidth(1.0)
        context.setStrokeColor(UIColor.red.cgColor)
        context.setFillColor(UIColor.red.cgColor)
        
        let rectangle = CGRect(x: 70, y: 50, width: 200, height: 100)
        context.addRect(rectangle)
        context.fill(rectangle)     // 그림에 색상 채우기
        context.strokePath()
        
        
        // Draw Circle
        context.setLineWidth(1.0)
        context.setStrokeColor(UIColor.blue.cgColor)
        context.setFillColor(UIColor.blue.cgColor)
        
        let circle = CGRect(x: 70, y: 200, width: 200, height: 100)
        context.addEllipse(in: circle)   // 사각형에 '내접'하는 원을 그린다.
        context.fillEllipse(in: circle)
        context.strokePath()
        
        // Draw Triangle
        context.setLineWidth(1.0)
        context.setStrokeColor(UIColor.green.cgColor)
        context.setFillColor(UIColor.green.cgColor)
        
        context.move(to: CGPoint(x: 170, y: 350))
        context.addLine(to: CGPoint(x: 270, y: 450))
        context.addLine(to: CGPoint(x: 70, y: 450))
        context.addLine(to: CGPoint(x: 170, y: 350))
        context.fillPath()      // 좌표를 연결한 영역에 색상을 채운다.
        context.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    @IBAction func btnDrawFlower(_ sender: UIButton) {
        UIGraphicsBeginImageContext(imgView.frame.size)
        let context = UIGraphicsGetCurrentContext()
        
        // Draw Triangle
        context?.setLineWidth(1.0)
        context?.setStrokeColor(UIColor.brown.cgColor)
        context?.setFillColor(UIColor.brown.cgColor)
        
        context?.move(to: CGPoint(x: 170, y: 200))
        context?.addLine(to: CGPoint(x: 200, y: 450))
        context?.addLine(to: CGPoint(x: 140, y: 450))
        context?.fillPath()      // 좌표를 연결한 영역에 색상을 채운다.
        context?.strokePath()
        
        // Draw Circle
        context?.setLineWidth(1.0)
        context?.setStrokeColor(UIColor.magenta.cgColor)

        context?.addEllipse(in: CGRect(x: 120, y: 150, width: 100, height: 100))
        context?.addEllipse(in: CGRect(x: 170, y: 150, width: 100, height: 100))
        context?.addEllipse(in: CGRect(x: 70, y: 150, width: 100, height: 100))
        context?.addEllipse(in: CGRect(x: 120, y: 100, width: 100, height: 100))
        context?.addEllipse(in: CGRect(x: 120, y: 200, width: 100, height: 100))
        context?.strokePath()
        
        imgView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
}
