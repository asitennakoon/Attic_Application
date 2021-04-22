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
  /* This component is used to upload a furniture to firebase */

  productForm: FormGroup;

  

  path: string = "";
  name: string = "";

  // input decorator to get the user account from login page.
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

  showContent:boolean



  constructor(private storage: StorageService, private firestore: FirestoreService) {
    // initialise variables
    this.progressBarVisible = false;
    this.modelUploaded = false;
    this.preview1Uploaded = false;
    this.preview2Uploaded = false;
    this.showContent=true;


    // initialise the form controls with validators.
    this.productForm = new FormGroup({
      'sceneTypes': new FormControl('', [Validators.required]),
      'furnituretypes': new FormControl('', [Validators.required]),
      'color': new FormControl('', [Validators.required]),
      'material': new FormControl('', [Validators.required]),
      'price': new FormControl('', [Validators.required]),
      'stock': new FormControl('', [Validators.required]),
      'description': new FormControl('', [Validators.required])

    });
    // console.log(this.firestore.getFurnitures())
  }

  ngOnInit(): void {
    // initialise the header for the product page
    this.header = {title: "ADD PRODUCT",account: this.user}
  }

  uploadModel($event) {
    // this function will trigger when upload button of the model is clicked.


    // console.log((this.user as IAccount)) ;

    // progressbar
    // this.progressBarVisible=true;

    // get the model and name from the event data.
    this.modelPath = $event.target.files[0];
    this.modelName = $event.target.files[0].name;


    console.log("Model name: " + this.modelName);

    // check whether the model is in the proper format
    if (!this.modelName.endsWith(".glb")) {
      this.modelUpload = "Invalid Model Type !"
      document.getElementById("modelMsg").style.color = "red";
    }

    else {
      // if the model is in proper format, a message will be shown and a flag will set to true.
      this.modelUpload = "Model Uploaded";
      this.modelUploaded = true;
    }
  }

  uploadImage1($event) {
    // function triggered when image 1 upload is clicked.
    this.image1Path = $event.target.files[0];
    this.image1Name = $event.target.files[0].name;

    // checks whether the image is in the required format ==> .png
    if (this.image1Name.endsWith(".jpg") ) {
      // if the file extension is correct, load the image and display.
      var reader = new FileReader();
      reader.readAsDataURL($event.target.files[0]);

      reader.onload = (event: any) => {
        this.image1Url = event.target.result;
        this.preview1Uploaded = true;
      }
    }
    else {
      // if the file is not in proper format, show the  default.
      this.image1Url = this.defaultImage;
    }

  }

  uploadImage2($event) {
    // function triggered when image 1 upload is clicked.
    this.image2Path = $event.target.files[0];
    this.image2Name = $event.target.files[0].name;

    // checks whether the image is in the required format ==> .gif
    if (this.image2Name.toLocaleLowerCase().endsWith(".gif")) {
      // if the file extension is correct, load the image and display.
      var reader = new FileReader();
      reader.readAsDataURL($event.target.files[0]);

      reader.onload = (event: any) => {
        this.image2Url = event.target.result;
        this.preview2Uploaded = true;
      }
    }
    else {
      // if the file is not in proper format, show the  default.
      this.image2Url = this.defaultImage;
    }

  }


  sendToFireStore(scene: any, type: any, count: number) {
    // this.progressBarVisible=true;

    // setTimeout(()=>{
    //   this.progressBarVisible=false;
    // }, 400);

    // get the next furniture number of the scene and type.
    
    console.log("count: "+count)

    // console.log("path: "+this.path);

    // Folder path of "Bedroom-Modern-Chair38.png" in the firebase is,
      //  images ==> gs://attic-b6655.appspot.com/images/Bedroom/Modern/Chair38.png (and .gif)
      //  model ==>  gs://attic-b6655.appspot.com/models/Bedroom/Modern/Chair38.glb

    //scene contains the room type and style type separated by "-". (ex: Bedroom-Modern)
    var splitIndex = Number(0);
    for(var i=0;i<scene.length;i++){
      if(scene.charAt(i) =="-"){
        splitIndex = i;
        break;
      }
    }
    var room = scene.substring(0,splitIndex);
    var style = scene.substring(splitIndex+1, scene.length+1);

    // send the files to the relevant locations in firebase
    let filename = "/objects/" + room + "/"+ style + "/" + type + count + ".glb";
    this.storage.sendToFireStorage(filename, this.modelPath);

    filename = "/images/" + room + "/"+ style + "/" + type + count + ".jpg";
    this.storage.sendToFireStorage(filename, this.image1Path);

    filename = "/images/" + room + "/"+ style + "/previews/" + type + count + ".gif";
    this.storage.sendToFireStorage(filename, this.image2Path);


  }

  onProductUpload() {
    // triggered when form upload button is clicked.
    console.log(this.productForm.value);
    let values = this.productForm.value;

    // if form is invalid
    if(!this.productForm.valid){
      window.alert("Invalid inputs !\n  *Please fill all the form data.\n  *Please check the selected types.\n  * Model file must be .glb format."
      +"\n  *Image 1 must be a .png file.\n  *Image 2 must be a .gif file.\n\n Try again !")
      return;
    }


    let selected_scene = values.sceneTypes;
    let selected_type = values.furnituretypes;
    // generating the furniture key (unique name) ex: Bedroom-Modern-Chair38
   let  n =this.firestore.getExistingCount(selected_scene, selected_type);
    let key: string = selected_scene + "-" + selected_type + n;

    console.log(selected_scene);

    // separate room type from scene.
    var splitIndex = Number(0);
      for(var i=0;i<selected_scene.length;i++){
        if(selected_scene.charAt(i) =="-"){
          splitIndex = i;
          break;
        }
      }
      var room = selected_scene.substring(0,splitIndex);

    // create a new furniture object with user data.
    let furniture: IFurniture = {
      $key: key,
      scene: selected_scene,
      type: selected_type,
      color: values.color,
      manufacturer: this.user.store,
      material: values.material,
      price: values.price,
      stock: values.stock,
      description: values.description,
      room: room,
    }


    // console.log(this.modelUploaded);
    // console.log(this.preview1Uploaded);
    // console.log(this.preview2Uploaded);


    // if all three flags are true, since the form is also valid, add the furniture to firestore.
    if (this.modelUploaded && this.preview1Uploaded && this.preview2Uploaded) {
      this.firestore.addFurniture(key, furniture);
      this.sendToFireStore(selected_scene,selected_type,n);

      
      window.alert("Furniture added successfully !");
      this.showContent=false;

    }
    else{
      window.alert("Model or images are missing")
    }
  }

}
