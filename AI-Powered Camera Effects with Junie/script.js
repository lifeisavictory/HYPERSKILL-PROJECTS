const video = document.getElementById('video');
const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d', { alpha: false });
const filterList = document.getElementById('filterList');
const status = document.getElementById('status');
const flipBtn = document.getElementById('flipBtn');
const pixelControls = document.getElementById('pixelControls');
const pixelateToggle = document.getElementById('pixelateToggle');
const pixelSizeInput = document.getElementById('pixelSize');
const pixelSizeValue = document.getElementById('pixelSizeValue');
const halftoneToggle = document.getElementById('halftoneToggle');
const halftoneControls = document.getElementById('halftoneControls');
const halftoneSpacingInput = document.getElementById('halftoneSpacing');
const halftoneSpacingValue = document.getElementById('halftoneSpacingValue');

// Off-screen canvas for performance optimization
const offscreenCanvas = document.createElement('canvas');
const offscreenCtx = offscreenCanvas.getContext('2d');

let currentFilter = 'none';
let isPixelated = false;
let pixelSize = 10;
let isHalftone = false;
let halftoneSpacing = 10;

async function initCamera() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({
            video: {
                facingMode: 'user',
                width: { ideal: 1080 },
                height: { ideal: 1080 }
            },
            audio: false
        });
        video.srcObject = stream;
        
        video.onloadedmetadata = () => {
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            requestAnimationFrame(renderLoop);
        };

        status.textContent = 'Camera active. Select a filter!';
    } catch (err) {
        console.error("Error accessing camera: ", err);
        status.textContent = 'Error: Could not access camera. Please ensure permissions are granted.';
    }
}

function renderLoop() {
    if (isPixelated || isHalftone) {
        // Effect using canvas
        const w = canvas.width;
        const h = canvas.height;
        
        ctx.clearRect(0, 0, w, h);
        ctx.imageSmoothingEnabled = false;
        
        if (isHalftone) {
            // Halftone effect
            // 1. Draw source to offscreen canvas with current filters
            offscreenCanvas.width = w;
            offscreenCanvas.height = h;
            offscreenCtx.filter = currentFilter;
            offscreenCtx.drawImage(video, 0, 0, w, h);
            
            // 2. Get image data to analyze brightness
            const imageData = offscreenCtx.getImageData(0, 0, w, h);
            const data = imageData.data;
            
            // 3. Clear and draw dots on the main canvas
            ctx.filter = 'none'; // Dots should be clean
            ctx.fillStyle = 'white';
            ctx.fillRect(0, 0, w, h);
            ctx.fillStyle = 'black';
            
            for (let y = 0; y < h; y += halftoneSpacing) {
                for (let x = 0; x < w; x += halftoneSpacing) {
                    const i = (y * w + x) * 4;
                    const r = data[i];
                    const g = data[i + 1];
                    const b = data[i + 2];
                    
                    // Calculate brightness (0-255)
                    const brightness = (0.299 * r + 0.587 * g + 0.114 * b);
                    // Darkness is the inverse
                    const darkness = 255 - brightness;
                    
                    // Radius depends on darkness
                    const radius = (darkness / 255) * (halftoneSpacing / 2) * 1.2;
                    
                    if (radius > 0.5) {
                        ctx.beginPath();
                        ctx.arc(x, y, radius, 0, Math.PI * 2);
                        ctx.fill();
                    }
                }
            }

            // If combined with pixelate, we apply it after halftone dots
            if (isPixelated) {
                const smallW = Math.max(1, Math.floor(w / pixelSize));
                const smallH = Math.max(1, Math.floor(h / pixelSize));
                
                offscreenCanvas.width = smallW;
                offscreenCanvas.height = smallH;
                offscreenCtx.filter = 'none';
                offscreenCtx.imageSmoothingEnabled = false;
                offscreenCtx.drawImage(canvas, 0, 0, smallW, smallH);
                
                ctx.clearRect(0, 0, w, h);
                ctx.imageSmoothingEnabled = false;
                ctx.drawImage(offscreenCanvas, 0, 0, smallW, smallH, 0, 0, w, h);
            }
        } else if (isPixelated) {
            // Pixelation effect (only)
            const smallW = Math.max(1, Math.floor(w / pixelSize));
            const smallH = Math.max(1, Math.floor(h / pixelSize));
            
            // Draw small version
            offscreenCanvas.width = smallW;
            offscreenCanvas.height = smallH;
            offscreenCtx.filter = currentFilter;
            offscreenCtx.drawImage(video, 0, 0, smallW, smallH);
            
            // Scale it back up
            ctx.filter = 'none';
            ctx.drawImage(offscreenCanvas, 0, 0, smallW, smallH, 0, 0, w, h);
        } else {
            // Other filters (when useCanvas is true but neither halftone nor pixelate are active - though updateDisplayMode should handle this)
            ctx.filter = currentFilter;
            ctx.drawImage(video, 0, 0, w, h);
        }
    }
    
    requestAnimationFrame(renderLoop);
}

filterList.addEventListener('click', (e) => {
    const btn = e.target.closest('.filter-btn');
    if (btn) {
        document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        
        const filterValue = btn.getAttribute('data-filter');
        currentFilter = filterValue;
        
        if (isPixelated || isHalftone) {
            // Filter is handled in renderLoop for canvas
            video.style.filter = 'none'; 
        } else {
            video.style.filter = currentFilter;
        }
    }
});

pixelateToggle.addEventListener('click', () => {
    isPixelated = !isPixelated;
    updateDisplayMode();
});


halftoneToggle.addEventListener('click', () => {
    isHalftone = !isHalftone;
    updateDisplayMode();
});

function updateDisplayMode() {
    const useCanvas = isPixelated || isHalftone;
    
    if (useCanvas) {
        video.style.display = 'none';
        canvas.style.display = 'block';
        video.style.filter = 'none';
    } else {
        video.style.display = 'block';
        canvas.style.display = 'none';
        video.style.filter = currentFilter;
    }

    // Update Pixelate UI
    if (isPixelated) {
        pixelateToggle.classList.add('active');
        pixelateToggle.textContent = 'Pixelate: ON';
        pixelControls.style.display = 'flex';
    } else {
        pixelateToggle.classList.remove('active');
        pixelateToggle.textContent = 'Pixelate: OFF';
        pixelControls.style.display = 'none';
    }


    // Update Halftone UI
    if (isHalftone) {
        halftoneToggle.classList.add('active');
        halftoneToggle.textContent = 'Halftone: ON';
        halftoneControls.style.display = 'flex';
    } else {
        halftoneToggle.classList.remove('active');
        halftoneToggle.textContent = 'Halftone: OFF';
        halftoneControls.style.display = 'none';
    }
}

pixelSizeInput.addEventListener('input', (e) => {
    pixelSize = parseInt(e.target.value);
    pixelSizeValue.textContent = pixelSize;
});


halftoneSpacingInput.addEventListener('input', (e) => {
    halftoneSpacing = parseInt(e.target.value);
    halftoneSpacingValue.textContent = halftoneSpacing;
});

flipBtn.addEventListener('click', () => {
    video.classList.toggle('mirrored');
    canvas.classList.toggle('mirrored');
});

// Initialize on load
window.addEventListener('load', initCamera);
