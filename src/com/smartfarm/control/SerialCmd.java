package com.smartfarm.control;

import com.smartfarm.tools.CRC8;

public class SerialCmd {

	public static final String TAG = "GreenHouse_Tablet";

	/**
	 * 操作：打开窗口1 485从机地址 04,明文协议：　“A6:001\r\n”
	 */
	public static final byte[] CMD_OPEN_WINDOW1 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口2 485从机地址 05,明文协议：　“A6:002\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW2 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口3 485从机地址 06,明文协议：　“A6:003\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW3 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口4 485从机地址 07,明文协议：　“A6:004\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW4 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口5 485从机地址 07,明文协议：　“A6:005\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW5 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口6 485从机地址 07,明文协议：　“A6:006\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW6 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口7 485从机地址 07,明文协议：　“A6:007\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW7 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x37,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x37), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口8 485从机地址 07,明文协议：　“A6:008\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW8 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x38,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x38), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开窗口9 485从机地址 07,明文协议：　“A6:009\r\n ”
	 */
	public static final byte[] CMD_OPEN_WINDOW9 = new byte[] {
			(byte) 0x41,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x39,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x39), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_OPEN_ALL = { CMD_OPEN_WINDOW1,
			CMD_OPEN_WINDOW2, CMD_OPEN_WINDOW3, CMD_OPEN_WINDOW4,
			CMD_OPEN_WINDOW5, CMD_OPEN_WINDOW6, CMD_OPEN_WINDOW7,
			CMD_OPEN_WINDOW8, CMD_OPEN_WINDOW9 };

	/**
	 * 操作：关闭窗口1 485从机地址 04,明文协议：　“A7:001\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW1 = new byte[] {
			0x41,
			0x37,
			(byte) 0x3A,
			0x30,
			0x30,
			0x31,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口2 485从机地址 05,明文协议：　“A7:002\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW2 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口3 485从机地址 06,明文协议：　“A7:003\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW3 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口4 485从机地址 07,明文协议：　“A7:004\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW4 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口5 485从机地址 07,明文协议：　“A7:005\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW5 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口6 485从机地址 07,明文协议：　“A7:006\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW6 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口7 485从机地址 07,明文协议：　“A7:007\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW7 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x37,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x37), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口8 485从机地址 07,明文协议：　“A7:008\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW8 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x38,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x38), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭窗口9 485从机地址 07,明文协议：　“A7:009\r\n ”
	 */
	public static final byte[] CMD_CLOSE_WINDOW9 = new byte[] {
			(byte) 0x41,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x39,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x39), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_CLOSE_ALL = { CMD_CLOSE_WINDOW1,
			CMD_CLOSE_WINDOW2, CMD_CLOSE_WINDOW3, CMD_CLOSE_WINDOW4,
			CMD_CLOSE_WINDOW5, CMD_CLOSE_WINDOW6, CMD_CLOSE_WINDOW7,
			CMD_CLOSE_WINDOW8, CMD_CLOSE_WINDOW9 };

	/**
	 * 操作：停止窗口1 485从机地址 04,明文协议：　“A8:001\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW1 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口2 485从机地址 05,明文协议：　“A8:002\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW2 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口3 485从机地址 06,明文协议：　“A8:003\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW3 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口4 485从机地址 07,明文协议：　“A8:004\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW4 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口5 485从机地址 07,明文协议：　“A8:005\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW5 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口6 485从机地址 07,明文协议：　“A8:006\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW6 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口7 485从机地址 07,明文协议：　“A8:007\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW7 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x37,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x37), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口8 485从机地址 07,明文协议：　“A8:008\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW8 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x38,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x38), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：停止窗口9 485从机地址 07,明文协议：　“A8:009\r\n ”
	 */
	public static final byte[] CMD_STOP_WINDOW9 = new byte[] {
			(byte) 0x41,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x39,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x39), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_STOP_ALL = { CMD_STOP_WINDOW1,
			CMD_STOP_WINDOW2, CMD_STOP_WINDOW3, CMD_STOP_WINDOW4,
			CMD_STOP_WINDOW5, CMD_STOP_WINDOW6, CMD_STOP_WINDOW7,
			CMD_STOP_WINDOW8, CMD_STOP_WINDOW9 };

	/**
	 * 操作：读取窗口1温度 明文协议：　“A4:001\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW1 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口2温度 明文协议：　“A4:002\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW2 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口3温度 明文协议：　“A4:003r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW3 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口4温度 明文协议：　“A4:004\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW4 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口5温度 明文协议：　“A4:005\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW5 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口6温度 明文协议：　“A4:006\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW6 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口7温度 明文协议：　“A4:007\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW7 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x37,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x37), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口8温度 明文协议：　“A4:008\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW8 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x38,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x38), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：读取窗口9温度 明文协议：　“A4:009\r\n ”
	 */
	public static final byte[] CMD_READ_TEMP_WINDOW9 = new byte[] {
			(byte) 0x41,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x39,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x39), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_READ_ALL = { CMD_READ_TEMP_WINDOW1,
			CMD_READ_TEMP_WINDOW2, CMD_READ_TEMP_WINDOW3,
			CMD_READ_TEMP_WINDOW4, CMD_READ_TEMP_WINDOW5,
			CMD_READ_TEMP_WINDOW6, CMD_READ_TEMP_WINDOW7,
			CMD_READ_TEMP_WINDOW8, CMD_READ_TEMP_WINDOW9 };

	/**
	 * 操作：读取照度 明文协议：　“D5:001\r\n”
	 */
	public static final byte[] CMD_LIGHT_READ = new byte[] {
			(byte) 0x44,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯1 明文协议：　“D2:001\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN1 = new byte[] {
			(byte) 0x44,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯2 明文协议：　“D2:002\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN2 = new byte[] {
			(byte) 0x44,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯3 明文协议：　“D2:003\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN3 = new byte[] {
			(byte) 0x44,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开补光灯4 明文协议：　“D2:004\r\n”
	 */
	public static final byte[] CMD_LIGHT_OPEN4 = new byte[] {
			(byte) 0x44,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_LIGHT_OPEN_ALL = { CMD_LIGHT_OPEN1,
			CMD_LIGHT_OPEN2, CMD_LIGHT_OPEN3, CMD_LIGHT_OPEN4 };

	/**
	 * 操作：关闭补光灯1 明文协议：　“D3:001\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE1 = new byte[] {
			(byte) 0x44,
			(byte) 0x33,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x33, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭补光灯2 明文协议：　“D3:002\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE2 = new byte[] {
			(byte) 0x44,
			(byte) 0x33,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x33, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭补光灯3 明文协议：　“D3:003\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE3 = new byte[] {
			(byte) 0x44,
			(byte) 0x33,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x33, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭补光灯4 明文协议：　“D3:004\r\n”
	 */
	public static final byte[] CMD_LIGHT_CLOSE4 = new byte[] {
			(byte) 0x44,
			(byte) 0x33,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x33, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_LIGHT_CLOSE_ALL = { CMD_LIGHT_CLOSE1,
			CMD_LIGHT_CLOSE2, CMD_LIGHT_CLOSE3, CMD_LIGHT_CLOSE4 };

	/**
	 * 操作：读取湿度 明文协议：　“D4:001\r\n”
	 */
	public static final byte[] CMD_WATER_READ1 = new byte[] {
			(byte) 0x44,
			(byte) 0x34,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x44, (byte) 0x34, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	public static final byte[] CMD_WATER_485_READ = new byte[] {
			(byte) 0x01,
			(byte) 0x03,
			(byte) 0x00,
			(byte) 0x00,
			(byte) 0x00,
			(byte) 0x02,
			CRC8.calcCrc8((byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x02), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开水泵 明文协议：　“G2:001\r\n”
	 */
	public static final byte[] CMD_WATER_BENG_OPEN = new byte[] {
			(byte) 0x47,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };
	/**
	 * 操作：关闭水泵 明文协议：　“G3:001\r\n”
	 */
	public static final byte[] CMD_WATER_BENG_CLOSE = new byte[] {
			(byte) 0x47,
			(byte) 0x33,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x33, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开药泵 明文协议：　“G7:001\r\n”
	 */
	public static final byte[] CMD_WATER_YAO_OPEN = new byte[] {
			(byte) 0x47,
			(byte) 0x37,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x37, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭药泵 明文协议：　“G8:001\r\n”
	 */
	public static final byte[] CMD_WATER_YAO_CLOSE = new byte[] {
			(byte) 0x47,
			(byte) 0x38,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x38, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };
	/**
	 * 操作：打开电磁阀 明文协议：　“G5:001\r\n”
	 */
	public static final byte[] CMD_WATER_OPEN1 = new byte[] {
			(byte) 0x47,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开电磁阀 明文协议：　“G5:002\r\n”
	 */
	public static final byte[] CMD_WATER_OPEN2 = new byte[] {
			(byte) 0x47,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开电磁阀 明文协议：　“G5:003\r\n”
	 */
	public static final byte[] CMD_WATER_OPEN3 = new byte[] {
			(byte) 0x47,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：打开电磁阀 明文协议：　“G5:004\r\n”
	 */
	public static final byte[] CMD_WATER_OPEN4 = new byte[] {
			(byte) 0x47,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };
	/**
	 * 操作：打开电磁阀 明文协议：　“G5:005\r\n”
	 */
	public static final byte[] CMD_WATER_OPEN5 = new byte[] {
			(byte) 0x47,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d, (byte) 0x0a };
	/**
	 * 操作：打开电磁阀 明文协议：　“G5:006\r\n”
	 */
	public static final byte[] CMD_WATER_OPEN6 = new byte[] {
			(byte) 0x47,
			(byte) 0x35,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x35, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_WATER_OPEN_ALL = { CMD_WATER_OPEN1,
			CMD_WATER_OPEN2, CMD_WATER_OPEN3, CMD_WATER_OPEN4, CMD_WATER_OPEN5,
			CMD_WATER_OPEN6 };

	/**
	 * 操作：关闭电磁阀 明文协议：　“G6:001\r\n”
	 */
	public static final byte[] CMD_WATER_CLOSE1 = new byte[] {
			(byte) 0x47,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭电磁阀 明文协议：　“G6:002\r\n”
	 */
	public static final byte[] CMD_WATER_CLOSE2 = new byte[] {
			(byte) 0x47,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭电磁阀 明文协议：　“G6:003\r\n”
	 */
	public static final byte[] CMD_WATER_CLOSE3 = new byte[] {
			(byte) 0x47,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d, (byte) 0x0a };

	/**
	 * 操作：关闭电磁阀 明文协议：　“G6:004\r\n”
	 */
	public static final byte[] CMD_WATER_CLOSE4 = new byte[] {
			(byte) 0x47,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d, (byte) 0x0a };
	/**
	 * 操作：关闭电磁阀 明文协议：　“G6:005\r\n”
	 */
	public static final byte[] CMD_WATER_CLOSE5 = new byte[] {
			(byte) 0x47,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d, (byte) 0x0a };
	/**
	 * 操作：关闭电磁阀 明文协议：　“G6:006\r\n”
	 */
	public static final byte[] CMD_WATER_CLOSE6 = new byte[] {
			(byte) 0x47,
			(byte) 0x36,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x47, (byte) 0x36, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d, (byte) 0x0a };

	public static final byte[][] CMD_WATER_CLOSE_ALL = { CMD_WATER_CLOSE1,
			CMD_WATER_CLOSE2, CMD_WATER_CLOSE3, CMD_WATER_CLOSE4,
			CMD_WATER_CLOSE5, CMD_WATER_CLOSE6 };
	/**
	 * 读取温度，湿度，二氧化碳浓度和光照度 例子：发送：：02 03 00 00 00 04 44 00 回应: 01 03 08 02 3F 07
	 * 88 0E 00 00 46 B9 BF 第 1 个数据为 02 3F(都是十六进制), 折成 10 进制方法:
	 * V=256*0x02+0x3F=575. 即为：575,即实际值为 575ppm。 温度值为07 88
	 * ，折成10进制为1928，实际值需除以100，测实际温度为19.28度。 湿度值为0E 00,
	 * 折成10进制为3690，实际值需除以100，测实际湿度为36.90%RH 。 光照度值为 00 46，折成 10 进制为 70，测实际光照度为
	 * 70lux 。
	 */
	public static final byte[] CMD_AIR_MISTURE = new byte[] { (byte) 0x02,
			(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04,
			(byte) 0x44, (byte) 0x3A };
	/**
	 * 单片机写入编号 A2:00?(CRC8 A2:00?!)/n ?当前id !修改id
	 * 
	 */
	public static final byte[] CMD_SET_DRIVACE1 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE2 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x32,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x32), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE3 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x33,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x33), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE4 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x34,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x34), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE5 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x35,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x35), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE6 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x36,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x36), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE7 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x37,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x37), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE8 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x38,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x38), (byte) 0x0d };

	public static final byte[] CMD_SET_DRIVACE9 = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x39,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x39), (byte) 0x0d };

	/*
	 * public static final byte[] CMD_SET_DRIVACE9=new byte[]{ (byte) 0x41,
	 * (byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x42,
	 * CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte)
	 * 0x30, (byte) 0x42), (byte) 0x0d };
	 */

	public static final byte[][] CMD_SET_DRIVACE_ALL = { CMD_SET_DRIVACE1,
			CMD_SET_DRIVACE2, CMD_SET_DRIVACE3, CMD_SET_DRIVACE4,
			CMD_SET_DRIVACE5, CMD_SET_DRIVACE6, CMD_SET_DRIVACE7,
			CMD_SET_DRIVACE8, CMD_SET_DRIVACE9 };
	// A1/r
	public static final byte[] COM_READ_DEVICE = new byte[] { (byte) 0x41,
			(byte) 0x31, (byte) 0x0d };
	// A1/r
	public static final byte[] COM_CLEAR_DEVICE = new byte[] { (byte) 0x41,
			(byte) 0x30, (byte) 0x0d };
	// A2:001
	public static final byte[] CMD_SET_DRIVACE = new byte[] {
			(byte) 0x41,
			(byte) 0x32,
			(byte) 0x3A,
			(byte) 0x30,
			(byte) 0x30,
			(byte) 0x31,
			CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A, (byte) 0x30,
					(byte) 0x30, (byte) 0x31), (byte) 0x0d };

	public static byte[] modifyDevice(int originalID, int newID) {
		originalID = originalID + 48;
		newID = newID + 48;

		byte[] s = new byte[] {
				(byte) 0x41,
				(byte) 0x32,
				(byte) 0x3A,
				(byte) 0x30,
				(byte) 0x30,
				(byte) originalID,
				(byte) newID,
				CRC8.calcCrc8((byte) 0x41, (byte) 0x32, (byte) 0x3A,
						(byte) 0x30, (byte) 0x30, (byte) originalID,
						(byte) newID), (byte) 0x0d };
		return s;
	}

}
