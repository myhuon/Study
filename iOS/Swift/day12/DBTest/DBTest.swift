/*

1. sqlite는 'local'에 저장되는 데이터이다.
2. 영구적으로 저장하기 위한 가장 기본적인 단위는 '파일'이다.

*/

//
//  ViewController.swift
//  DBTest
//
//  Created by hyunsoo on 2022/07/08.
//

import UIKit
import SQLite3

class ViewController: UIViewController {

    let dbManager = DBManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func btnOpenDatabase(_ sender: UIButton) {
        dbManager.openDatabase()
    }
    
    @IBAction func btnCreateTable(_ sender: UIButton) {
        dbManager.createTable()
    }
    
    @IBAction func btnInsert(_ sender: UIButton) {
        dbManager.insert(name_value: "test1")
    }
    
    @IBAction func btnSelectAll(_ sender: UIButton) {
        dbManager.selectAll(name_value: "test1")
    }
    
    @IBAction func btnUpdate(_ sender: UIButton) {
        dbManager.update(name_value: "송현수", id_value: 1)
    }
    
    @IBAction func btnDelete(_ sender: UIButton) {
        dbManager.delete(id_value: 1)
    }
    
    @IBAction func btnDropTable(_ sender: UIButton) {
        dbManager.dropTable()
    }
    
    @IBAction func btnCloseDatabase(_ sender: UIButton) {
        dbManager.closeDatabase()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        dbManager.closeDatabase()
    }
    
}

