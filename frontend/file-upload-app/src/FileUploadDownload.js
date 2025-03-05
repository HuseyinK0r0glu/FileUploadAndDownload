import React , {useState , useEffect} from "react"

const FileDownloadUpload = () => {

    const [files , setFiles] = useState([]);
    const [selectedFile , setSelectedFile] = useState(null);
    const [uploadFiles , setUploadFiles] = useState(null);
    const [uploadStatus, setUploadStatus] = useState(""); 
    const [isUploading, setIsUploading] = useState(false); 

    useEffect(() => {

        const fetchFiles = async () => {
            
            try{
                const response = await fetch('http://localhost:8080/file/files');
                if(!response.ok){
                    throw new Error("Network repsponse was not ok");
                }

                const data = await response.json();
                setFiles(data);

            }catch(err){
                console.log("Error fetching files : " + err);
            }

        }

        fetchFiles();

    },[]);

    const handleFileChange = (e) => {
        setUploadFiles(e.target.files);
    }

    const handleUpload = async () => {

        if(!uploadFiles){
            setUploadStatus("❌ Please select a file to upload.");
            return;
        }

        const formData = new FormData();
        Array.from(uploadFiles).forEach((file) => {
            formData.append("files" , file);
        })

        setIsUploading(true);
        setUploadStatus("⏳ Uploading...");

        try{
            const response = await fetch("http://localhost:8080/file/upload", {
                method : "POST",
                body : formData
            });
            
            if(response.ok){
                const data = await response.json();
                console.log("Upload successful:", data);
                setFiles((prevFiles) => Array.from(new Set([...prevFiles, ...data])));
                setUploadStatus("✅ Upload successful!");
            }else {
                setUploadStatus(`❌ Upload failed: ${response.status}`);
            }
        }catch(err){
            setUploadStatus("❌ Upload error: " + err.message);
        }
    };

    const handleDownload = async () => {

        if(!selectedFile) return;

        try{
            const response = await fetch(`http://localhost:8080/file/download/${selectedFile}`);

            if(response.ok){
                const fileBlob = await response.blob();
                const url = URL.createObjectURL(fileBlob);
                const a = document.createElement("a");
                a.href = url;
                a.download = selectedFile;
                a.click();
                URL.revokeObjectURL(url);
            }else {
                console.error("Error downloading file");
            }

        }catch(err){
            console.error("Error downloading file:", err);
        }

    };

    return (
        <div className="container mt-4">
        <h1 className="text-center mb-4">File Upload & Download</h1>
  
        {/* Upload Section */}
        <div className="card p-3 mb-4 shadow">
          <h2>Upload Files</h2>
          <input type="file" multiple className="form-control mb-2" onChange={handleFileChange} />
          <button className="btn btn-primary" onClick={handleUpload}>
            Upload
          </button>
          {uploadStatus && <p className="mt-2">{uploadStatus}</p>}
        </div>
  
        {/* Download Section */}
        <div className="card p-3 shadow">
          <h2>Download Files</h2>
          <select
            className="form-select mb-2"
            value={selectedFile}
            onChange={(e) => setSelectedFile(e.target.value)}
          >
            <option value="">Select a file to download</option>
            {files.map((file, index) => (
              <option key={`${file}-${index}`} value={file}>
                {file}
              </option>
            ))}
          </select>
          <button className="btn btn-success" onClick={handleDownload}>
            Download
          </button>
        </div>
      </div>
    );
}

export default FileDownloadUpload;