/**
 * OSS直传工具类
 * 提供前端直传OSS的功能
 */
class OssDirectUpload {
    constructor(baseUrl) {
        this.baseUrl = baseUrl || '';
        this.ossConfig = null;
    }

    /**
     * 获取OSS签名配置
     */
    async getOssSignature() {
        try {
            const response = await fetch(`${this.baseUrl}/api/oss/signature`);
            const result = await response.json();
            if (result.code === 200) {
                this.ossConfig = result.data;
                return this.ossConfig;
            } else {
                throw new Error(result.msg || '获取OSS签名失败');
            }
        } catch (error) {
            console.error('获取OSS签名失败:', error);
            throw error;
        }
    }

    /**
     * 生成唯一文件名
     */
    async generateFileName(originalFilename) {
        try {
            const response = await fetch(`${this.baseUrl}/api/oss/generateFileName?originalFilename=${encodeURIComponent(originalFilename)}`);
            const result = await response.json();
            if (result.code === 200) {
                return result.data;
            } else {
                throw new Error(result.msg || '生成文件名失败');
            }
        } catch (error) {
            console.error('生成文件名失败:', error);
            throw error;
        }
    }

    /**
     * 直传文件到OSS
     * @param {File} file 要上传的文件
     * @param {Function} onProgress 进度回调函数
     * @param {Function} onSuccess 成功回调函数
     * @param {Function} onError 错误回调函数
     */
    async uploadFile(file, onProgress, onSuccess, onError) {
        try {
            // 获取OSS配置
            if (!this.ossConfig) {
                await this.getOssSignature();
            }

            // 生成唯一文件名
            const fileName = await this.generateFileName(file.name);
            const key = this.ossConfig.dir + fileName;

            // 构建FormData
            const formData = new FormData();
            formData.append('key', key);
            formData.append('policy', this.ossConfig.policy);
            formData.append('OSSAccessKeyId', this.ossConfig.accessid);
            formData.append('success_action_status', '200');
            formData.append('signature', this.ossConfig.signature);
            formData.append('file', file);

            // 创建XMLHttpRequest进行上传
            const xhr = new XMLHttpRequest();

            // 监听上传进度
            if (onProgress) {
                xhr.upload.addEventListener('progress', (event) => {
                    if (event.lengthComputable) {
                        const percentComplete = (event.loaded / event.total) * 100;
                        onProgress(percentComplete);
                    }
                });
            }

            // 监听上传完成
            xhr.addEventListener('load', () => {
                if (xhr.status === 200) {
                    const fileUrl = this.ossConfig.host + '/' + key;
                    if (onSuccess) {
                        onSuccess({
                            url: fileUrl,
                            key: key,
                            fileName: fileName
                        });
                    }
                } else {
                    const error = new Error(`上传失败，状态码: ${xhr.status}`);
                    if (onError) {
                        onError(error);
                    }
                }
            });

            // 监听上传错误
            xhr.addEventListener('error', () => {
                const error = new Error('网络错误，上传失败');
                if (onError) {
                    onError(error);
                }
            });

            // 发送请求
            xhr.open('POST', this.ossConfig.host);
            xhr.send(formData);

        } catch (error) {
            console.error('上传文件失败:', error);
            if (onError) {
                onError(error);
            }
        }
    }

    /**
     * 使用Promise方式上传文件
     * @param {File} file 要上传的文件
     * @returns {Promise} 返回Promise对象
     */
    uploadFilePromise(file) {
        return new Promise((resolve, reject) => {
            this.uploadFile(
                file,
                (progress) => {
                    // 可以在这里处理进度
                    console.log(`上传进度: ${progress.toFixed(2)}%`);
                },
                (result) => {
                    resolve(result);
                },
                (error) => {
                    reject(error);
                }
            );
        });
    }
}

// 全局实例
window.OssDirectUpload = OssDirectUpload;

// 如果是模块环境，导出类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = OssDirectUpload;
}
