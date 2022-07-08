//
//  DBManager.swift
//  DBTest
//
//  Created by Dongduk4 on 2022/07/08.
//

import Foundation
import SQLite3

class DBManager {
    
    let DB_NAME = "my_db.sqlite"
    let TABLE_NAME = "my_table"
    let COL_ID = "id"
    let COL_NAME = "name"

    var db: OpaquePointer? = nil
    
    func openDatabase() {
        let dbFile = try! FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false).appendingPathComponent(DB_NAME)     // 앱에 지정한 공간이 없으면 생성, 있으면 가져와서 사용. 앱의 공간에 할당
        
        if sqlite3_open(dbFile.path, &db) == SQLITE_OK {    // 포인터를 이용해서 데이터베이스 열기
            print("Successfully Opened")
            print(dbFile)
            /*
             file:///Users/dongduk4/Library/Developer/CoreSimulator/Devices/0D2B67B1-2138-4DF5-8078-28389B099627/data/Containers/Data/Application/37D9208A-E980-451E-8941-EE377A7818B2/Documents/my_db.sqlite
             */
        } else {
            print("Unable to open DB")
        }
    }
    
    func createTable() {
        let createTableString = """
            CREATE TABLE IF NOT EXISTS \(TABLE_NAME) ( \(COL_ID) INTEGER PRIMARY KEY AUTOINCREMENT,
                                                       \(COL_NAME) TEXT);
        """     // 여러 문장에 걸쳐 문장을 쓰고자 할 때
        var createTableStmt: OpaquePointer?     // 텍스트 문장이 complite된 sql 문장을 담을 수 있는 포인터
    
        print("TABLE SQL: \(createTableString)")
        
        if sqlite3_prepare_v2(db, createTableString, -1, &createTableStmt, nil) == SQLITE_OK {  // 컴파일된 sql 문장이 담긴 포인터 준비
            if sqlite3_step(createTableStmt) == SQLITE_DONE {   // 포인터를 이용하여 sql 실행
                print("Successfully created.")
            }
            sqlite3_finalize(createTableStmt)   // 포인터 해제
        } else {
            let error = String(cString: sqlite3_errmsg(db)!)
            print("Table Error: \(error)")
        }
    }
    
    func insert(name_value: String) {
        var insertStmt: OpaquePointer?
        let sql = "insert into \(TABLE_NAME) values (null, ?)"
        
        if sqlite3_prepare_v2(db, sql, -1, &insertStmt, nil) == SQLITE_OK {
            let SQLITE_TRANSIENT = unsafeBitCast(-1, to: sqlite3_destructor_type.self)  // 아래의 name 문자열 끝나는 지점 표시하기 위해서 필요
            
            if sqlite3_bind_text(insertStmt, 1, name_value, -1, SQLITE_TRANSIENT) != SQLITE_OK {  // 첫 번째 ? 컬럼에 name 값을 겠다 (text 타입을 바인딩 하겠다.)
                let errmsg = String(cString: sqlite3_errmsg(db)!)
                print("Text Binding Failure: \(errmsg)")
                return
            }
            
            if sqlite3_step(insertStmt) == SQLITE_DONE {
                print("Successfully inserted.")
            } else {
                print("insert error")
            }
            
            sqlite3_finalize(insertStmt)
        } else {
            print("Insert statement is not prepared.")
        }
        
    }
    
    func selectAll(name_value: String) {
       // let sql = "select * from \(TABLE_NAME)  where \(COL_NAME) = '\(name_value)'"  // 값을 넣을 때는 '' 안에 넣어줘야한다.
        let sql = "select * from \(TABLE_NAME)"
        var queryStmt: OpaquePointer?
        
        if sqlite3_prepare(db, sql, -1, &queryStmt, nil) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Reading Error : \(errmsg)")
            return
        }
        
        while(sqlite3_step(queryStmt) == SQLITE_ROW) {  // 아직 읽을 row가 있다면 아래 문장 실행
            let id = sqlite3_column_int(queryStmt, 0)   // 0번째 컬럼을 읽어온다. (0번째 컬럼이 integer타입이라 int로 읽어옴)
            let name = String(cString: sqlite3_column_text(queryStmt, 1))   // Text니까 String으로 타입캐스팅 해줘야함.
            print("id: \(id) name: \(name)")
        }
        
        sqlite3_finalize(queryStmt)     // 포인터 해제
    }
    
    func update(name_value: String, id_value: Int) {
        let query = "update \(TABLE_NAME) set \(COL_NAME) = ? where \(COL_ID) = ?"
        var updateStmt: OpaquePointer?
        
        // 1. prepare
        if sqlite3_prepare(db, query, -1, &updateStmt, nil) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("error preparing update : \(errmsg)")
            return
        }
        
        let SQLITE_TRANSIENT = unsafeBitCast(-1, to: sqlite3_destructor_type.self)
        
        // 2. binding : prepared statement 매개변수 연결
        if sqlite3_bind_text(updateStmt, 1, name_value, -1, SQLITE_TRANSIENT) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Text Binding Faliure : \(errmsg)")
            return
        }
        
        if sqlite3_bind_int(updateStmt, 2, Int32(id_value)) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Integer Binding Faliure : \(errmsg)")
            return
        }
        
        // 3. 실행
        if sqlite3_step(updateStmt) != SQLITE_DONE {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Update Failure : \(errmsg)")
            return
        }
        
        sqlite3_finalize(updateStmt)
    }
    
    func delete(id_value: Int) {
        let query = "delete from \(TABLE_NAME) where \(COL_ID) = ?"
        var deleteStmt: OpaquePointer?
        
        if sqlite3_prepare(db, query, -1, &deleteStmt, nil) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("error preparing stmt : \(errmsg)")
            return
        }
        
        bindIntParams(deleteStmt!, no: 1, param: id_value)
        
        if sqlite3_step(deleteStmt) != SQLITE_DONE {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Delete Failure : \(errmsg)")
            return
        }
        
        sqlite3_finalize(deleteStmt)
    }
    
    func dropTable() {
        // preparedStatement를 사용하는 것이 아니라 sql문을 바로 사용하는 방식.
        if sqlite3_exec(db, "drop table if exists \(TABLE_NAME)", nil, nil, nil) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Drop Error : \(errmsg)")
            return
        }
    }
    
    func closeDatabase() {
        if sqlite3_close(db) != SQLITE_OK {   // pointer 이용해서 데이터베이스 닫기
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Database Close Error : \(errmsg)")
            return
        } else {
            print("Database Close Successfully!")
        }
    }
    
    func bindTextParams(_ stmt: OpaquePointer, no: Int, param: String) {
        let SQLITE_TRANSIENT = unsafeBitCast(-1, to: sqlite3_destructor_type.self)
        
        if sqlite3_bind_text(stmt, Int32(no), param, -1, SQLITE_TRANSIENT) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Text Binding Failure : \(errmsg)")
            return
        }
    }
    
    func bindIntParams(_ stmt: OpaquePointer, no: Int, param: Int) {
        if sqlite3_bind_int(stmt, Int32(no), Int32(param)) != SQLITE_OK {
            let errmsg = String(cString: sqlite3_errmsg(db)!)
            print("Integer Binding Failure : \(errmsg)")
            return
        }
    }
}
