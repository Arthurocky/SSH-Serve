$(function() {

	const Xterm = {

		_term: null,

		init() {
			this._term = new Terminal({
				cursorBlink: true,
				cursorStyle: "block",
				scrollback: 1200,
				screenKeys: true,
				cursor: 'help',
				rows: 49,
				cols: 150
			})

			this._term.open(document.getElementById("terminal"), true)
			this._consoleBase()

			/* this._term.on('data', data => {
				this._term.write(data)
			}) */

			return this._term
		},

		reset() {
			this._term.reset()
			this._consoleBase()
		},

		_consoleBase() {
			this._term.writeln('欢迎~~')
			this._term.writeln('Welcome to WebSSH')
			// this._term.writeln('Author: LiJunpeng')
			//this._term.writeln('Dev Date: 2022-04-04')
			this._term.writeln('Demo Version: 1.0.5')
			this._term.write('You can click on the connect button to connect to the terminal...')
		}
	}

	const Socket = {

		_socket: null,

		create() {
			if (this._socket) {
				this.close()
			}

			let protocol = window.location.protocol
			if (protocol.endsWith('http:')) {
				protocol = 'ws://'
			} else {
				protocol = 'wss://'
			}

			this._socket = new WebSocket(protocol + window.location.host + '/ssh')
			// this._socket = new WebSocket(protocol + window.location.hostname + ':8000/ssh')

			return this._socket
		},

		close() {
			if (this.is()) {
				this._socket.close()
				this._socket = null
			}
		},

		send(data) {
			this._socket.send(JSON.stringify(data))
		},

		is() {
			return this._socket !== null
		}

	}

	const term = Xterm.init()

	term.on('data', data => {
		if (!Socket.is()) {
			return
		}

		Socket.send({
			type: 'command',
			message: data
		})
	})

	document.getElementById('connectBtn').onclick = () => {
		const
			host = document.getElementById('host').value,
			port = document.getElementById('port').value,
			username = document.getElementById('username').value,
			password = document.getElementById('password').value

		if (!host) {
			alert('请输入主机地址')
			return
		}

		if (!port) {
			alert('请输入端口号')
			return
		}

		if (!username) {
			alert('请输入用户名')
			return
		}

		if (!password) {
			alert('请输入密码')
			return
		}

		term.reset()
		Socket.close()

		const socket = Socket.create()
		socket.onopen = () => {


			term.write(`Connecting to ${host}:${port}...`)

			Socket.send({
				type: 'connect',
				message: {
					host,
					port,
					username,
					password
				}
			})
		}

		socket.onmessage = e => {
			if (e.data.endsWith("Connection failed.")) {
				Socket.close()
			}
			
			// 解决添加了cols属性后，文本超出时没有折行并将文本覆盖原有内容的bug
			if (e.data.length === 3) {
				term.write(e.data.trim())
			} else {
				term.write(e.data)	
			}
			
			term.focus()
		}
	}

})
