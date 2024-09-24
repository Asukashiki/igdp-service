import ddddocr
from PIL import Image
from io import BytesIO
import base64

# detect captcha, open source project: https://github.com/sml2h3/ddddocr.git
def detect_Captcha(base64_address):
    base64_image_data = base64_address
    if "," in base64_image_data:
        base64_image_data = base64_image_data.split(',')[1]
        
    image_data = base64.b64decode(base64_image_data)
    image_file = BytesIO(image_data)
    image = Image.open(image_file)
    image.save('example.jpg')
    ocr = ddddocr.DdddOcr()
    image = open("example.jpg", "rb").read()
    result = ocr.classification(image)

    return result