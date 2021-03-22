import { Component, Input, OnInit } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/storage';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IAccount } from 'src/app/Interfaces/IAccount';
import { IFurniture } from 'src/app/Interfaces/IFurniture';
import { FirestoreService } from 'src/app/Services/firestore/firestore.service';
import { StorageService } from 'src/app/Services/storage/storage.service';


@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  productForm: FormGroup;

  path: string = "";
  name: string = "";

  @Input() user!: IAccount;
  header;


  sceneTypes: string[] = this.firestore.getSceneTypes();
  furnituretypes: string[] = this.firestore.getFurnitureTypes();

  modelPath: string = "";
  modelName: string = "";
  modelUpload: string = "Upload 3D Model of the Product";
  modelUploaded: boolean;

  image1Path: string = "";
  image1Name: string = "";
  preview1Uploaded: boolean;

  image2Path: string = "";
  image2Name: string = "";
  preview2Uploaded: boolean;



  defaultImage: string = "../../../assets/images/np.png";
  image1Url: string = this.defaultImage;
  image2Url: string = this.defaultImage;

  progressBarVisible: boolean;





  constructor(private storage: StorageService, private firestore: FirestoreService) {
    this.progressBarVisible = false;
    this.modelUploaded = false;
    this.preview1Uploaded = false;
    this.preview2Uploaded = false;



    this.productForm = new FormGroup({
      'sceneTypes': new FormControl('', [Validators.required]),
      'furnituretypes': new FormControl('', [Validators.required]),
      'color': new FormControl('', [Validators.required]),
      'material': new FormControl('', [Validators.required]),
      'price': new FormControl('', [Validators.required]),
      'stock': new FormControl('', [Validators.required]),
      'description': new FormControl('', [Validators.required])

    });




    console.log(this.firestore.getFurnitures())



  }

  ngOnInit(): void {
    this.header = {title: "ADD PRODUCT",account: this.user}
  }

  uploadModel($event) {
    
    console.log((this.user as IAccount))

    // this.progressBarVisible=true;

    this.modelPath = $event.target.files[0];
    this.modelName = $event.target.files[0].name;

    // setTimeout(()=>{
    //   this.progressBarVisible=false;
    // }, 400);
    console.log("Model name: " + this.modelName);

    if (!this.modelName.endsWith(".glb")) {
      this.modelUpload = "Invalid Model Type !"
      document.getElementById("modelMsg").style.color = "red";
    }

    else {
      this.modelUpload = "Model Uploaded";
      this.modelUploaded = true;
    }
  }

  uploadImage1($event) {
    this.image1Path = $event.target.files[0];
    this.image1Name = $event.target.files[0].name;

    if (this.image1Name.endsWith(".png") ) {
      var reader = new FileReader();
      reader.readAsDataURL($event.target.files[0]);

      reader.onload = (event: any) => {
        this.image1Url = event.target.result;
        this.preview1Uploaded = true;
      }
    }
    else {
      this.image1Url = this.defaultImage;
    }

  }

  uploadImage2($event) {
    this.image2Path = $event.target.files[0];
    this.image2Name = $event.target.files[0].name;

    if (this.image2Name.toLocaleLowerCase().endsWith(".gif")) {
      var reader = new FileReader();
      reader.readAsDataURL($event.target.files[0]);

      reader.onload = (event: any) => {
        this.image2Url = event.target.result;
        this.preview2Uploaded = true;
      }
    }
    else {
      this.image2Url = this.defaultImage;
    }

  }


  sendToFireStore(scene: string, type: string) {
    // this.progressBarVisible=true;

    // setTimeout(()=>{
    //   this.progressBarVisible=false;
    // }, 400);
    let count = this.firestore.getExistingCount(scene, type);

    console.log(this.path);
    let filename = "/objects/" + scene + "/" + type + count + ".glb";
    this.storage.sendToFireStore(filename, this.modelPath);

    filename = "/images/" + scene + "/" + type + count + ".png";
    this.storage.sendToFireStore(filename, this.image1Path);

    filename = "/images/" + scene + "/" + type + count + ".gif";
    this.storage.sendToFireStore(filename, this.image2Path);


  }

  onProductUpload() {
    console.log(this.productForm.value);
    let values = this.productForm.value;

    if(!this.productForm.valid){
      window.alert("Invalid inputs !")
      return;
    }


    let selected_scene = values.sceneTypes;
    let selected_type = values.furnituretypes;
    let key: string = selected_scene + "-" + selected_type + this.firestore.getExistingCount(selected_scene, selected_type);

    console.log(key)

    let furniture: IFurniture = {
      $key: key,
      scene: selected_scene,
      type: selected_type,
      colour: values.color,
      manufacturer: this.user.store,
      material: values.material,
      price: values.price,
      stock: values.stock,
      description: values.description,


    }


    console.log(this.modelUploaded);
    console.log(this.preview1Uploaded);
    console.log(this.preview2Uploaded);



    if (this.modelUploaded && this.preview1Uploaded && this.preview2Uploaded) {
      this.firestore.addFurniture(key, furniture);
      this.sendToFireStore(selected_scene,selected_type);
    }
    else{
      window.alert("Model and images are missing")
    }
  }

}
