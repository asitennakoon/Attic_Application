import { Component, OnInit } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/storage';
import { FormControl, FormGroup } from '@angular/forms';


@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  productForm: FormGroup;

  path: String="";
  name: String ="";

  modelUpload: string ="Upload 3D Model of the Product";

  defaultImage: string="../../../assets/images/np.png";
  url: string=this.defaultImage;

  progressBarVisible:boolean;


  constructor(private  firestore:AngularFireStorage) { 
    this.progressBarVisible=false;

    this.productForm = new FormGroup({
      'color': new FormControl(''),
      'manufacturer': new FormControl(''),
      'material': new FormControl(''),
      'price': new FormControl(''),
      'stock': new FormControl(''),
      'description': new FormControl('')

    });

  }

  ngOnInit(): void {
    
  }

  upload($event){
    this.progressBarVisible=true;
      
    

      this.path=$event.target.files[0];
      this.name=$event.target.files[0].name;
  
      setTimeout(()=>{
        this.progressBarVisible=false;
      }, 400);

       if(this.name.endsWith(".png")|| this.name.endsWith(".jpg")){
        var reader = new FileReader();
        reader.readAsDataURL($event.target.files[0]);
     
        reader.onload=(event: any)=>{
         this.url=event.target.result;
          }
         }
         else{
           this.url=this.defaultImage;
         }

         this.modelUpload="Model Uploaded";
  }

  uploadImage(){
    this.progressBarVisible=true;

    setTimeout(()=>{
      this.progressBarVisible=false;
    }, 400);

    console.log(this.path);
    this.firestore.upload("/testWeb/"+this.name, this.path );

    

  }

  onProductUpload(){

  }

}
