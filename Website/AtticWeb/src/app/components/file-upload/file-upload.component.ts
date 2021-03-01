import { Component, OnInit } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/storage';


@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  path: String="";
  name: String ="";
  url: string="";

  progressBarVisible:boolean;


  constructor(private  firestore:AngularFireStorage) { 
    this.progressBarVisible=false;

  }

  ngOnInit(): void {
    
  }

  upload($event){
    this.progressBarVisible=true;


    this.path=$event.target.files[0];
    this.name=$event.target.files[0].name;

    if(this.name.endsWith(".png")){
   var reader = new FileReader();
   reader.readAsDataURL($event.target.files[0]);

   reader.onload=(event: any)=>{
    this.url=event.target.result;
   }
  }


   this.progressBarVisible=false;
  }

  uploadImage(){
    this.progressBarVisible=true;

    console.log(this.path);
    this.firestore.upload("/testWeb/"+this.name, this.path );

    this.progressBarVisible=false;

  }

}
